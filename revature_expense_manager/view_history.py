from data_store import load_expenses, load_users
import pandas as pd

expenses = load_expenses()
users = load_users()

report_list = []
# load all expenses with user_id only if expense is not pending
def view_expense_report(user):
    for expense in expenses:
        if expense["user_id"] == user["id"] and expense["status"] != "pending":
            report_list.append(expense)

    df = pd.DataFrame(report_list)
    print(df.to_string(index=False))



