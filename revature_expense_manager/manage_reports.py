from data_store import load_expenses, load_users, save_expenses
import pandas as pd
import logging
import view_history

#i will upload the specified users pending reports, handle the actions, and save the new data to json

#load everyone's info because I will overwrite the entire file
def delete_report(user):
    logging.info(f"User {user['username']} requested to delete a report")
    expenses = load_expenses()
    users = load_users()
    print(user)
    data = []
    #display their info
    for expense in expenses:
        data.append(expense)

    user_data_list = []
    #load on the specific users pending reports
    for block in data:
        if(user["id"] == block["user_id"] and block["status"] == "pending"):
            user_data_list.append(block)

    #read the users reports to them. pending only
    print("\n" * 5)
    print("Your pending reports:")

    df = pd.DataFrame(user_data_list)
    df = df.drop(columns=["user_id"])
    print(df.to_string(index=False))
    print("")

    while True:
        try:
            id_to_delete = int(input("Select the report ID you would like to delete: "))
            break
        except ValueError:
            print("Invalid. Enter a report ID")

    found = False
    #loop entire json, take out specified data by id, overwrite
    for block in data:
        if(id_to_delete == block["id"]):
            logging.info(f"Expense report with ID {id_to_delete} deleted by user '{user.get('username', 'Unknown')}'")
            data.remove(block)
            found = True
            break

    if not found:
        logging.warning(
            f"Delete attempt failed: report ID {id_to_delete} not found by user '{user.get('username', 'Unknown')}'")
        print("ID not found")

    #data removed from entire list. now overwrite
    save_expenses(data)

def edit_report(user):
    logging.info(f"User '{user.get('username', 'Unknown')}' requested to edit an expense report")
    expenses = load_expenses()
    users = load_users()
    print(user)
    data = []
    #display their info
    for expense in expenses:
        data.append(expense)

    user_data_list = []
    #load on the specific users pending reports
    for block in data:
        if(user["id"] == block["user_id"] and block["status"] == "pending"):
            user_data_list.append(block)

    #read the users reports to them. pending only
    print("\n" * 5)
    print("Your pending reports:")

    df = pd.DataFrame(user_data_list)
    df = df.drop(columns=["user_id"])
    print(df.to_string(index=False))
    print("")

    while True:
        try:
            id_to_edit = int(input("Select the report ID you would like to edit: "))
            new_amount = float(input("Enter your new dollar amount: "))
            new_desc = str(input("Enter your new description: "))
            break
        except ValueError:
            print("Invalid input. Please try again.")

    found = False
    #loop entire json, take out specified data by id, overwrite
    for block in data:
        if(id_to_edit == block["id"]):
            logging.info(f"Expense report with ID {id_to_edit} edited by user '{user.get('username', 'Unknown')}'")
            block["amount"] = new_amount
            block["description"] = new_desc
            found = True
            break

    if not found:
        logging.warning(
            f"Edit attempt failed: report ID {id_to_edit} not found by user '{user.get('username', 'Unknown')}'")
        print("ID not found")

    save_expenses(data)