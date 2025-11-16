from data_store import load_expenses, load_users, save_expenses
import pandas as pd
import view_history

#i will upload the specified users pending reports, handle the actions, and save the new data to json

users = load_users()


#load everyone's info because I will overwrite the entire file
def delete_report(user):
    expenses = load_expenses()
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

    #try except here
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
            data.remove(block)
            found = True
            break

    if not found:
        print("ID not found")

    #data removed from entire list. now overwrite
    save_expenses(data)







def edit_report(user):
    pass