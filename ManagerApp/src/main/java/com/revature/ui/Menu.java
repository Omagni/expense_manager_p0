package com.revature.ui;
import java.util.Scanner;
import com.revature.service.*;
import com.revature.model.User;
import java.util.logging.Logger;

public class Menu {
    private static final Logger logger = Logger.getLogger(Menu.class.getName());

    private Scanner scanner = new Scanner(System.in);
    private User user;

    private String menuOptions = "1. Review pending expenses" +
            "\n2. Approve/Deny submitted expenses" +
            "\n3. Generate reports by category";

    AuthServiceInterface authService = new AuthServiceImpl();
    ManageService expenseViewService = new ManageServiceImpl();

    public void showMenu() {

        logger.info("Showing login menu.");

        System.out.println("--Login Screen--");

        while (user == null) {

            System.out.print("Please enter your username: ");
            String username = scanner.nextLine().toLowerCase();

            System.out.print("Please enter your password: ");
            String password = scanner.nextLine();

            user = authService.loginUser(username, password);

            if (user != null) {
                logger.info("Login successful for user: " + user.getUsername());
                System.out.println("Login successful! Welcome, " + user.getUsername());
            } else {
                logger.warning("Login failed for username: " + username);
                System.out.println("Login failed! Please try again.\n");
            }
        }

        System.out.println("\n\n");

        logger.info("Entering selection menu.");
        selectionMenu();
    }

    // shows list of options user can choose from
    public void selectionMenu() {

        int selection;

        // loop options until logout
        while(true){
            System.out.println("----------Menu Options----------");
            System.out.println(menuOptions);

            try {
                selection = Integer.parseInt(scanner.nextLine());
                logger.info("User selected menu option: " + selection);
            } catch (NumberFormatException e) {
                logger.warning("Invalid input for menu selection.");
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch(selection){
                case 1:
                    logger.info("User selected to view pending expense reports.");
                    System.out.println("View pending expense reports selected");
                    expenseViewService.viewPendingExpenses();
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    logger.info("User selected to approve/deny pending expenses.");
                    System.out.println("Approve/deny pending expenses selected");
                    expenseViewService.updateStatus(user.getUserId());
                    break;
                case 3:
                    logger.info("User selected to generate reports by type.");
                    System.out.println("Generate reports by type");
                    expenseViewService.generateReportsByType();
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    break;
                default:
                    logger.warning("User selected invalid menu option: " + selection);
                    System.out.println("Option invalid. Please select options 1-3");
                    break;
            }

            System.out.println("\n\n");

        }

    }

}