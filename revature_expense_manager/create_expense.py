#here i would need to accept parameters
#take those parameters
#and write to the appropriate json file

#i don't need the user data to write to the json
#i can just upload any correct data to the json
#then i could filter it only when actually viewing history

from datetime import datetime, timezone
from data_store import load_expenses, load_users, save_expenses

def generate_expense(user):
    expenses = load_expenses()
    users = load_users()

    user_id = user["id"]
    expense_id = 100
    #generate new expense id
    for id in expenses:
        if (id not in expenses):
            expense_id = id
        expense_id += 1

    amount = float(input("Please enter the dollar amount: "))
    amount = f"{amount:.2f}"
    desc = input("Please enter the description of the expense: ")
    date = datetime.now(timezone.utc)
    formatted_date = date.strftime("%Y-%m-%dT%H:%M:%SZ")
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

    data.append(new_entry)
    save_expenses(data)