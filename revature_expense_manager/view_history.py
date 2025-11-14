from data_store import load_expenses, load_users

expenses = load_expenses()
users = load_users()

# load all expenses with user_id only if expense is not pending
def view_expense_report(user):
    for expense in expenses:
        if expense["user_id"] == user["id"]:
            print(expense)


