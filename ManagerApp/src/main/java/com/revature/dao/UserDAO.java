package com.revature.dao;

import com.revature.model.User;

public interface UserDAO {

    User findUserByUsername(String username);
}