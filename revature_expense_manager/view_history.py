from data_store import load_expenses, load_users
import pandas as pd
                            #maybe I need to check if the list is empty

# load all expenses with user_id only if expense is not pending
def view_expense_report(user):
    expenses = load_expenses()
    users = load_users()
    report_list = []
    for expense in expenses:
        if expense["user_id"] == user["id"] and expense["status"] != "pending":
            report_list.append(expense)

    if not report_list:
        print("The list is empty")
        return

    df = pd.DataFrame(report_list)
    print(df.to_string(index=False))

def view_all_reports(user):
    expenses = load_expenses()
    report_list = []
    for expense in expenses:
        if expense["user_id"] == user["id"]:
            report_list.append(expense)

    if not report_list:
        print("The list is empty")
        return

    df = pd.DataFrame(report_list)
    print(df.to_string(index=False))