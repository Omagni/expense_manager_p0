#here we need to have a reference to every single username and password
#import json
#from data_store import load_users
import logging
import db_manager


def login(username, password):
    user = db_manager.load_user_by_username(username)
    if user and user["password"] == password:
        logging.info(f"User '{username}' logged in successfully")
        print("User logged in")
        return user
    else:
        logging.warning(f"Failed login attempt for username: {username}")
        return None

    # old func for loading in the user from json
    # for user in users:
    #     if user["username"].lower() == username.lower() and user["password"] == password:
    #         logging.info(f"User '{username}' logged in successfully")
    #         print("User logged in")
    #         return user # we return the entire user here
    #     else:
    #         break

    # so we have the user authenticated
    # we can pass the information pertaining just to the user to another module that will
    # only let them see their information

