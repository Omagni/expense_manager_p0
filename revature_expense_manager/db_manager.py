import sqlite3
import os
import logging

BASE_DIR = os.path.dirname(__file__)  # python folder
DB_FILE = os.path.abspath(os.path.join(BASE_DIR, "..", "sql.db"))

def get_conn():
    return sqlite3.connect(DB_FILE)
