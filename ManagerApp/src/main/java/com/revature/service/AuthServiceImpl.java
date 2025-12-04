package com.revature.service;

import java.util.List;
import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;
import java.util.Scanner;

public class AuthServiceImpl implements AuthServiceInterface {

    private UserDAO userDAO = new UserDAOImpl();
    private String username;
    private String password;

    private Scanner scanner = new Scanner(System.in);

    public AuthServiceImpl() {
    }

    @Override
    public User loginUser() {

        // request username / password
        System.out.println("Please enter your username: ");
        username = scanner.next().toLowerCase();
        System.out.println("Please enter your password: ");
        password = scanner.next();

        // searches the DB for a user specified by userName
        User user = userDAO.findUserByUsername(username);

        // verify user exits
        if (user == null){
            System.out.println("AuthService: user not found");
            return null;
        } else {
            System.out.println("AuthService: user found!");
        }

        // verify password matches
        if (password.equals(user.getPassword())){
            System.out.println("Password matches");
        } else{
            System.out.println("Password does not match");
        }

        // only allows managers to login
        if (checkUserRole(user.getRole())) {
            return user;
        } else {
            return null;
        }
    }

    // checks if user is a manager
    @Override
    public boolean checkUserRole(String role){
        return role.equals("manager");
    }
}