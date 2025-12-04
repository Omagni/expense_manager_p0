package com.revature.ui;
import java.util.Scanner;
import com.revature.service.*;
import com.revature.model.User;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private User user;

    private String menuOptions = "1. Review pending expenses" +
            "\n2. Approve/Deny submitted expenses" +
            "\n3. Generate reports by category";

    // SERVICES
    AuthServiceInterface authService = new AuthServiceImpl();
    ManageService expenseViewService = new ManageServiceImpl();

    public void showMenu() {

        System.out.println("--Login Screen--");
        // loop until manager login
        while (user == null) {
            user = authService.loginUser();
            if (user != null) {
                System.out.println("Login successful! Welcome, " + user.getUsername());
                System.out.println("\n\n\n");
            } else {
                System.out.println("Login failed! Please try again.");
            }
        }
        selectionMenu();
    }

    // shows list of options user can choose from
    public void selectionMenu() {

        int selection;

        // loop options until logout
        while(true){
            System.out.println("----------Menu Options----------");
            System.out.println(menuOptions);
            selection = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch(selection){
                case 1:
                    System.out.println("View pending expense reports selected");
                    expenseViewService.viewPendingExpenses();
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Approve/deny pending expenses selected");
                    expenseViewService.updateStatus(user.getUserId());
                    break;
                case 3:
                    System.out.println("Generate reports by type");
                    expenseViewService.generateReportsByType();
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("Option invalid. Please select options 1-3");
                    break;
            }

            System.out.println("\n\n");
        }
    }

}