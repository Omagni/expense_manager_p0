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

    new_amount = -1
    while True:
        try:
            # Loop to validate ID
            while True:
                id_to_edit = int(input("Select the report ID you would like to edit: "))
                found = False
                for block in data:
                    if id_to_edit == block["id"]:
                        found = True
                        break
                if not found:
                    print(f"{id_to_edit} is not available.")
                else:
                    break  # valid ID, exit ID loop

            # Loop to validate new_amount
            while True:
                try:
                    new_amount = float(input("Enter your new dollar amount: "))
                    if 1 <= new_amount <= 1000:
                        new_amount = f"{new_amount:.2f}"
                        break  # valid amount, exit amount loop
                    else:
                        print("Invalid input. Please enter a dollar amount between $1 - $1,000.")
                except ValueError:
                    print("Invalid input. Please enter a numeric value.")

            # Loop to validate description
            while True:
                new_desc = input("Enter your new description: ")
                if new_desc.strip() == "" or new_desc.isnumeric() or len(new_desc) < 10 or len(new_desc) > 50:
                    print("Invalid input. Description must be between 10 and 50 characters and not numeric or empty.\n")
                else:
                    break  # valid description

            break  # all inputs valid, exit outermost loop

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