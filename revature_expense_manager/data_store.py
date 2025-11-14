import json

USERS_FILE = "user_info.json"
EXPENSE_FILE = "expense_reports.json"

def load_users():
    with open(USERS_FILE, "r") as f:
        return json.load(f)

def save_users(data):
    with open(USERS_FILE, "w") as f:
        json.dump(data, f, indent=2)

def load_expenses():
    with open(EXPENSE_FILE, "r") as f:
        return json.load(f)

def save_expenses(data):
    with open(EXPENSE_FILE, "w") as f:
        json.dump(data, f, indent=2)
