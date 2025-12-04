package com.revature.service;

import java.util.List;
import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;
public class AuthServiceImpl implements AuthServiceInterface {

    private UserDAO userDAO = new UserDAOImpl();
//    private String username;
//    private String password;

    public AuthServiceImpl() {
    }

    @Override
    public User loginUser(String username, String password) {

        User user = userDAO.findUserByUsername(username);

        if (user == null) {
            System.out.println("AuthService: user not found");
            return null;
        }

        // check password
        if (!password.equals(user.getPassword())) {
            System.out.println("Password does not match");
            return null;
        }

        // check role
        if (!checkUserRole(user.getRole())) {
            System.out.println("AuthService: user is not a manager");
            return null;
        }

        return user;
    }

    // checks if user is a manager
    @Override
    public boolean checkUserRole(String role){
        return role.equals("manager");
    }
}