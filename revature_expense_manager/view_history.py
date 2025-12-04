#from data_store import load_expenses, load_users
import pandas as pd
import logging
import db_manager
from tabulate import tabulate
                            #maybe I need to check if the list is empty

# load all expenses with user_id only if expense is not pending
def view_expense_report(user):
    #expenses = load_expenses() OLD dict from json data_store
    report_list = db_manager.load_processed_expenses_for_user(user["id"])

    #users = load_users() OLD JSON

    # for expense in expenses:
    #     if expense["user_id"] == user["id"] and expense["status"] != "pending":
    #         report_list.append(expense)

    if not report_list:
        logging.info(f"User '{user.get('username', 'Unknown')}' viewed empty expense history")
        print("The list is empty")
        return

    logging.info(f"User '{user.get('username', 'Unknown')}' viewed processed expense reports")
    df = pd.DataFrame(report_list)
    #print(df.to_string(index=False))
    print(tabulate(df, headers='keys', tablefmt='grid', showindex=False))

def view_all_reports(user):
    #expenses = load_expenses() old json
    report_list = db_manager.load_expenses_for_user(user["id"])

    # for expense in expenses:
    #     if expense["user_id"] == user["id"]:
    #         report_list.append(expense)

    if not report_list:
        logging.info(f"User '{user.get('username', 'Unknown')}' viewed empty expense report list")
        print("The list is empty")
        return

    logging.info(f"User '{user.get('username', 'Unknown')}' viewed all expense reports")
    df = pd.DataFrame(report_list)
    #print(df.to_string(index=False))
    print(tabulate(df, headers='keys', tablefmt='grid', showindex=False))
