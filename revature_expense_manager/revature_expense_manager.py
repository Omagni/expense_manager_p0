import json
import pandas as pd
import auth, view_history, create_expense, manage_reports

print("Welcome to the Revature Expense Manager!")
print("             Login Page")

# login page
user_logged_in = False
while(user_logged_in == False):
    #we will pass the username and password into auth and auth will take care of the rest
    username = input("Username: ")
    password = input("Password: ")

    user = auth.login(username, password) # save the user into the user variable. Can access anywhere now

    if user is None:
        print("User not found. Try again")

    if user:
        user_logged_in = True

def print_menu():
    print("\n" * 10)
    print("***Revature Expense Manager***")
    print('1. View history of your processed expense reports')
    print("2. Generate a new expense")
    print("3. View status of all reports ")
    print("4. Edit a pending report")
    print("5. Delete a pending report")
    print("6. Logout")
    print("\n")

def app():
    while True:
        print_menu()

        try:
            user_selection = int(input("Enter your choice: "))
        except ValueError:
            print("Invalid input. Please enter a number")
            continue

        if(user_selection == 1):
            print("\n" * 5)
            print("Expense Report History:")
            view_history.view_expense_report(user)

        elif(user_selection == 2):
            print("\n" * 5)
            print("Generate New Report:")
            create_expense.generate_expense(user)

        elif(user_selection == 3):
            print("\n" * 5)
            print("Status of all reports: ")
            view_history.view_all_reports(user)

        elif(user_selection == 4):
            print("\n" * 5)
            print('Edit your pending report: ')
            manage_reports.edit_report(user)

        elif(user_selection == 5):
            print("\n" * 5)
            print("Delete your pending report: ")
            manage_reports.delete_report(user)

        elif(user_selection == 6):
            print("Logging out and exiting app...")
            exit()

        else:
            print("Invalid. Please enter a selection between 1-6")
            continue

        while True:
            try:
                print("\n1. Back to list view")
                print("2. Logout and exit")
                user_selection = int(input("Enter your choice: "))
            except ValueError:
                print("!!!Input Error. Please enter 1 or 2!!!")
                continue

            if(user_selection == 1):
                break
            elif(user_selection == 2):
                print("Logging out and exiting app...")
                exit()
            else:
                print("Invalid input. Enter a value between 1-2")
                continue

app()