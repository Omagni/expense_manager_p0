import sqlite3
import os


BASE_DIR = os.path.dirname(__file__)
DB_FILE = os.path.abspath(os.path.join(BASE_DIR, "..", "revature_expense.db"))

def get_conn():
    return sqlite3.connect(DB_FILE)

def load_user_by_username(username):
    conn = get_conn()
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute("SELECT * FROM user_info WHERE username = ?", (username,))
    row = cur.fetchone()
    conn.close()
    if row:
        return dict(row)
    return None

def load_expenses_for_user(user_id):
    conn = get_conn()
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute("""
        SELECT
            expense_reports.id,
            expense_reports.user_id,
            expense_reports.amount,
            expense_reports.description,
            expense_reports.date,
            approvals.status AS approval_status,
            approvals.comment,
            approvals.reviewer,
            approvals.review_date
        FROM expense_reports
        LEFT JOIN approvals ON expense_reports.id = approvals.expense_id
        WHERE expense_reports.user_id = ?
    """, (user_id,))
    rows = cur.fetchall()
    conn.close()
    return [dict(row) for row in rows]

def load_processed_expenses_for_user(user_id):
    conn = get_conn()
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute("""
        SELECT
            expense_reports.id,
            expense_reports.user_id,
            expense_reports.amount,
            expense_reports.description,
            expense_reports.date,
            approvals.status AS approval_status,
            approvals.comment,
            approvals.reviewer,
            approvals.review_date
        FROM expense_reports
        JOIN approvals ON expense_reports.id = approvals.expense_id
        WHERE expense_reports.user_id = ? AND approvals.status != 'pending'
    """, (user_id,))
    rows = cur.fetchall()
    conn.close()
    return [dict(row) for row in rows]

def load_pending_reports(user_id):
    conn = get_conn()
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute("""
        SELECT
            expense_reports.id,
            expense_reports.user_id,
            expense_reports.amount,
            expense_reports.description,
            expense_reports.date,
            approvals.status AS approval_status,
            approvals.comment,
            approvals.reviewer,
            approvals.review_date
        FROM expense_reports
        JOIN approvals ON expense_reports.id = approvals.expense_id
        WHERE expense_reports.user_id = ? AND approvals.status = 'pending'
    """, (user_id,))
    rows = cur.fetchall()
    conn.close()
    return [dict(row) for row in rows]

#loads every single expense in data
def load_all_expenses():
    conn = get_conn()
    conn.row_factory = sqlite3.Row
    cur = conn.cursor()
    cur.execute("""
        SELECT
            expense_reports.id,
            expense_reports.user_id,
            expense_reports.amount,
            expense_reports.description,
            expense_reports.date,
            approvals.status AS approval_status,
            approvals.comment,
            approvals.reviewer,
            approvals.review_date
        FROM expense_reports
        LEFT JOIN approvals ON expense_reports.id = approvals.expense_id
    """)
    rows = cur.fetchall()
    conn.close()
    return [dict(row) for row in rows]


def insert_expense(expense):
    conn = get_conn()
    cur = conn.cursor()

    # insert into expense_reports without status
    cur.execute("""
        INSERT INTO expense_reports (id, user_id, amount, description, date)
        VALUES (?, ?, ?, ?, ?)
    """, (expense['id'], expense['user_id'], expense['amount'], expense['description'], expense['date']))

    # insert initial approval record with status 'pending'
    cur.execute("""
        INSERT INTO approvals (expense_id, status)
        VALUES (?, 'pending')
    """, (expense['id'],))

    conn.commit()
    conn.close()
