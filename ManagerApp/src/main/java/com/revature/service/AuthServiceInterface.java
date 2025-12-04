package com.revature.service;

import com.revature.model.User;

public interface AuthServiceInterface {

    public User loginUser(String username, String password);
    public boolean checkUserRole(String role);
}
