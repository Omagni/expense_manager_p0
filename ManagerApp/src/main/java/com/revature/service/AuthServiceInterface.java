package com.revature.service;

import com.revature.model.User;

public interface AuthServiceInterface {

    public User loginUser();
    public boolean checkUserRole(String role);
}
