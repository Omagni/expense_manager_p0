import logging
import json

#*******************************************************************
#THIS IS AN OLD MODULE THAT USED JSON TO IMPORT DATA INTO A DICT
#THIS HAS BEEN UPDATED TO db_manager.py WHERE WE TURN DATABASE ITEMS
#INTO A DICT
#*******************************************************************
USERS_FILE = "user_info.json"
EXPENSE_FILE = "expense_reports.json"

def load_users():
    try:
        with open(USERS_FILE, "r") as f:
            return json.load(f)
    except Exception as e:
        logging.error(f"Error loading users file: {e}")
        return []

def save_users(data):
    try:
        with open(USERS_FILE, "w") as f:
            json.dump(data, f, indent=2)
    except Exception as e:
        logging.error(f"Error saving users file: {e}")

def load_expenses():
    try:
        with open(EXPENSE_FILE, "r") as f:
            return json.load(f)
    except Exception as e:
        logging.error(f"Error loading expenses file: {e}")
        return []

def save_expenses(data):
    try:
        with open(EXPENSE_FILE, "w") as f:
            json.dump(data, f, indent=2)
    except Exception as e:
        logging.error(f"Error saving expenses file: {e}")
