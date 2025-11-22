#here i would need to accept parameters
#take those parameters
#and write to the appropriate json file

#i don't need the user data to write to the json
#i can just upload any correct data to the json
#then i could filter it only when actually viewing history
import logging
from datetime import datetime, timezone
from data_store import load_expenses, load_users, save_expenses

def generate_expense(user):
    expenses = load_expenses()
    users = load_users()

    user_id = user["id"]
    expense_id = 100
    ids_found = []
    for expense in expenses:
        ids_found.append(expense['id'])

    while expense_id in ids_found:
        expense_id += 1

    amount = -1
    while True:
        try:
            amount_input = input("Please enter the dollar amount: $")
            amount = float(amount_input)

            if 1 <= amount <= 1000:
                break
            else:
                print("Invalid input. Please enter a dollar amount between $1 - $1,000.")
        except ValueError:
            print("Invalid input. Please enter a numeric value.")

    amount = f"{amount:.2f}"
    desc = input("Please enter the description of the expense: ")
    while(desc.strip() == "" or desc.isnumeric() or len(desc) < 10 or len(desc) > 50):
        print("Invalid input. Description must be between 10 and 50 characters and not numeric or empty.\n")
        desc = input("Please enter the description of the expense: ")

    date = datetime.now(timezone.utc)
    formatted_date = date.strftime("%Y-%m-%d")
    status = "pending"

    #load current json right now
    data = load_expenses()

    new_entry = {
        "id": expense_id,
        "user_id": user_id,
        "amount": amount,
        "description": desc,
        "date": formatted_date,
        "status": status
    }

    logging.info(f"New expense created by user {user['username']} with id {expense_id} amount ${amount}")
    data.append(new_entry)
    save_expenses(data)