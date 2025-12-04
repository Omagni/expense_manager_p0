import logging
import pandas as pd
import db_manager
import sqlite3
from tabulate import tabulate

def delete_report(user):
    logging.info(f"User {user['username']} requested to delete a report")

    pending = db_manager.load_pending_reports(user["id"])

    if not pending:
        print("\nNo pending reports to delete.\n")
        return

    print("\n" * 3)
    print("Your pending reports:")
    df = pd.DataFrame(pending)
    print(tabulate(df.drop(columns=["user_id"]), headers='keys', tablefmt='grid', showindex=False))
    print("")

    while True:
        try:
            id_to_delete = int(input("Select the report ID you would like to delete: "))
            if any(r["id"] == id_to_delete for r in pending):
                break
            print("Invalid ID. Choose an ID from the list.")
        except ValueError:
            print("Enter a valid number.")

    conn = db_manager.get_conn()
    cur = conn.cursor()

    # Delete related approvals first
    cur.execute("DELETE FROM approvals WHERE expense_id = ?", (id_to_delete,))

    # Then delete the expense report
    cur.execute("DELETE FROM expense_reports WHERE id = ? AND user_id = ?", (id_to_delete, user["id"]))

    conn.commit()
    conn.close()

    logging.info(f"Expense report ID {id_to_delete} deleted by user {user['username']}")
    print("\nReport deleted successfully.\n")


def edit_report(user):
    logging.info(f"User {user['username']} requested to edit a report")

    pending = db_manager.load_pending_reports(user["id"])

    if not pending:
        print("\nNo pending reports to edit.\n")
        return

    print("\n" * 3)
    print("Your pending reports:")
    df = pd.DataFrame(pending)
    #print(df.drop(columns=["user_id"]).to_string(index=False))
    print(tabulate(df.drop(columns=["user_id"]), headers='keys', tablefmt='grid', showindex=False))
    print("")

    while True:
        try:
            id_to_edit = int(input("Select the report ID you want to edit: "))
            if any(r["id"] == id_to_edit for r in pending):
                break
            print("Invalid ID. Try again.")
        except ValueError:
            print("Enter a valid number.")

    while True:
        try:
            new_amount = float(input("Enter new dollar amount: "))
            if 1 <= new_amount <= 1000:
                new_amount = f"{new_amount:.2f}"
                break
            print("Amount must be between $1 - $1,000.")
        except ValueError:
            print("Enter a valid number.")

    while True:
        new_desc = input("Enter new description: ")
        if new_desc.strip() and not new_desc.isnumeric() and 10 <= len(new_desc) <= 50:
            break
        print("Description must be 10–50 chars, not empty, not numeric.")

    conn = db_manager.get_conn()
    cur = conn.cursor()
    cur.execute("""
        UPDATE expense_reports
        SET amount = ?, description = ?
        WHERE id = ? AND user_id = ?
    """, (new_amount, new_desc, id_to_edit, user["id"]))
    conn.commit()
    conn.close()

    logging.info(f"Expense report ID {id_to_edit} edited by user {user['username']}")
    print("\nReport updated successfully.\n")

# def load_pending_reports(user_id):
#     conn = db_manager.get_conn()
#     conn.row_factory = sqlite3.Row
#     cur = conn.cursor()
#     cur.execute("""
#         SELECT * FROM expense_reports
#         WHERE user_id = ? AND status = 'pending'
#     """, (user_id,))
#     rows = cur.fetchall()
#     conn.close()
#     return [dict(r) for r in rows]
