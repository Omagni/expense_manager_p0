import json
import pandas as pd
import auth, view_history

# login page

#we will pass the username and password into auth and auth will take care of the rest
username = input("Username: ")
password = input("Password: ")

user = auth.login(username, password) # save the user into the user variable. Can access anywhere now

if user is None:
    print("Invalid. Exiting.")
    exit()



# view report
view_history.view_expense_report(user)


# create new report

# view status of all reports

# view handled reports