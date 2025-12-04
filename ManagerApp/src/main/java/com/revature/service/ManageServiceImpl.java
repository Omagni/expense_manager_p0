package com.revature.service;

import com.revature.dao.ExpenseReportDAO;
import com.revature.dao.ExpenseReportDAOImpl;
import com.revature.model.CategoryReport;
import com.revature.model.ExpenseReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ManageServiceImpl implements ManageService {

    private static final Logger logger = Logger.getLogger(ManageServiceImpl.class.getName());

    private Scanner scanner = new Scanner(System.in);
    private ExpenseReportDAO expenseDao = new ExpenseReportDAOImpl();

    public void viewPendingExpenses() {
        logger.info("Viewing pending expenses.");
        List<ExpenseReport> reports = expenseDao.getPendingExpenseReports();

        if (reports == null || reports.isEmpty()) {
            logger.info("No pending expense reports found.");
            System.out.println("No pending expense reports.");
            return;
        }

        System.out.printf(
                "%-5s %-8s %-10s %-50s %-12s%n",
                "ID", "UserID", "Amount", "Description", "Date"
        );
        System.out.println(
                "----------------------------------------------------------------------------------------------"
        );

        // rows
        for (ExpenseReport r : reports) {
            System.out.printf(
                    "%-5d %-8d %-10.2f %-50s %-12s%n",
                    r.getId(),
                    r.getUser_id(),
                    r.getAmount(),
                    (r.getDescription().trim()),
                    r.getDate()
            );
        }
    }



    public void updateStatus(int reviewer_id){

        logger.info("updateStatus() called by reviewer_id: " + reviewer_id);

        String options = "1. approve\n2. deny\n3. cancel";

        // show all pending reports
        viewPendingExpenses();
        System.out.println("Select an expense record by ID to approve/deny");

        // select record user wants to update
        int expenseRecordSelected = scanner.nextInt();
        logger.info("User selected expense record ID: " + expenseRecordSelected);
        scanner.nextLine(); // clear buffer

        // status update options
        int optionSelection = 0;
        while(optionSelection < 1 || optionSelection > 3){
            System.out.println(options);
            System.out.println("Please select an option between 1 and 3");
            optionSelection = scanner.nextInt();
            logger.info("User selected option: " + optionSelection);
        }

        scanner.nextLine(); // clear buffer

        switch (optionSelection) {
            case 1: // "approved" option
                if (expenseDao.updateStatus(expenseRecordSelected, "approved", reviewer_id)){
                    logger.info("Expense record " + expenseRecordSelected + " approved by reviewer " + reviewer_id);
                    // do nothing
                } else{
                    logger.warning("Failed to approve expense record " + expenseRecordSelected);
                    System.out.println("Record could not be updated");
                    return;
                }
                break;
            case 2: // "denied" option
                if (expenseDao.updateStatus(expenseRecordSelected, "denied", reviewer_id)){
                    logger.info("Expense record " + expenseRecordSelected + " denied by reviewer " + reviewer_id);
                    // do nothing
                } else{
                    logger.warning("Failed to deny expense record " + expenseRecordSelected);
                    System.out.println("Record could not be updated");
                    return;
                }

                break;
            case 3: // cancel
                logger.info("Update status cancelled by user");
                return;
            default:
                logger.warning("Invalid option selected in updateStatus(): " + optionSelection);
                System.out.println("Invalid option selected");
        }

        System.out.println("Update expense report comment? (y/n)");
        logger.info("Asking user if want to update comment for expense record " + expenseRecordSelected);
        String userInput = scanner.next().toLowerCase();
        scanner.nextLine(); // clear buffer
        if (userInput.equals("y")){
            logger.info("User chose to update comment for expense record " + expenseRecordSelected);
            System.out.println("Enter comment: ");
            String newComment = scanner.nextLine();
            expenseDao.updateComment(expenseRecordSelected, newComment);
        }
    }

    public void updateComment(){
        logger.info("updateComment() called");
        System.out.println("Select record by id: ");
        int selectedRecord = scanner.nextInt();
        logger.info("User selected record ID for comment update: " + selectedRecord);
        scanner.nextLine(); // clear buffer
        System.out.println("Enter comment: ");
        String newComment = scanner.nextLine();
        logger.info("New comment entered: " + newComment);
        try{
            expenseDao.updateComment(selectedRecord, newComment);
        } catch (Exception e){
            logger.warning("Failed to update comment: " + e.getMessage());
            System.out.println(e.getMessage());
        }

    }

    public void generateReportsByType(){
        logger.info("generateReportsByType() called");
        System.out.println("Generate report by:");
        System.out.println("1. Employee username");
        System.out.println("2. Category");
        System.out.println("3. Date Range");

        int choice = scanner.nextInt();
        scanner.nextLine();
        logger.info("User selected report type option: " + choice);

        switch (choice) {
            case 1:
                getExpenseReportByUsername();
                break;
            case 2:
                getExpenseReportsByCategory();
                break;
            case 3:
                getExpenseReportsByDate();
                break;
            default:
                logger.warning("Invalid report type option selected: " + choice);
                System.out.println("Invalid option.");
                break;
        }
    }

public void getExpenseReportByUsername() {
    logger.info("getExpenseReportByUsername() called");
    System.out.println("Enter username");
    String username = scanner.nextLine();
    logger.info("Username entered: " + username);

    List<CategoryReport> list = expenseDao.getExpenseReportByUsername(username);

    if (list == null || list.isEmpty()) {
        logger.info("No reports found for username: " + username);
        System.out.println("No reports found.");
        return;
    }

    logger.info("Found " + list.size() + " reports for username: " + username);

    System.out.printf(
            "%-5s %-8s %-10s %-55s %-12s %-10s %-10s %-40s %-12s%n",
            "ID",
            "UserID",
            "Amount",
            "Description",
            "Date",
            "Status",
            "Reviewer",
            "Comment",
            "ReviewDate"
    );

    System.out.println(
            "------------------------------------------------------------------------------------------------------------------------------------------------------------"
    );

    for (CategoryReport cr : list) {
        System.out.printf(
                "%-5d %-8d %-10.2f %-55s %-12s %-10s %-10d %-40s %-12s%n",
                cr.getId(),
                cr.getUser_id(),
                cr.getAmount(),
                (cr.getDescription().trim()),
                cr.getDate(),
                cr.getStatus(),
                cr.getReviewer(),
                cr.getComment(),
                cr.getReview_date()
        );
    }
}

public void getExpenseReportsByCategory() {
    logger.info("getExpenseReportsByCategory() called");
    System.out.println("1. Approved expense reports");
    System.out.println("2. Denied expense reports");

    int option = scanner.nextInt();
    scanner.nextLine(); // clear buffer
    logger.info("Category report option selected: " + option);

    String status;
    if (option == 1) {
        status = "approved";
    } else if (option == 2) {
        status = "denied";
    } else {
        logger.warning("Invalid category report option selected: " + option);
        System.out.println("Invalid option.");
        return;
    }

    List<CategoryReport> list = expenseDao.getExpenseReportsByCategory(status);

    if (list == null || list.isEmpty()) {
        logger.info("No " + status + " reports found.");
        System.out.println("No " + status + " reports found.");
        return;
    }

    logger.info("Found " + list.size() + " " + status + " reports");

    System.out.printf(
            "%-5s %-8s %-10s %-40s %-12s %-10s %-10s %-40s %-12s%n",
            "ID",
            "UserID",
            "Amount",
            "Description",
            "Date",
            "Status",
            "Reviewer",
            "Comment",
            "ReviewDate"
    );

    System.out.println(
            "-----------------------------------------------------------------------------------------------------------------------------------------"
    );

    // ROWS
    for (CategoryReport cr : list) {
        System.out.printf(
                "%-5d %-8d %-10.2f %-40s %-12s %-10s %-10d %-40s %-12s%n",
                cr.getId(),
                cr.getUser_id(),
                cr.getAmount(),
                cr.getDescription().trim(),
                cr.getDate(),
                cr.getStatus(),
                cr.getReviewer(),
                cr.getComment(),
                cr.getReview_date()
        );
    }
}

    public void getExpenseReportsByDate() {
        logger.info("getExpenseReportsByDate() called");
        System.out.println("Enter start date (YYYY-MM-DD)");
        String dateStart = scanner.next();

        System.out.println("Enter end date (YYYY-MM-DD)");
        String dateEnd = scanner.next();

        scanner.nextLine(); // clear buffer

        List<CategoryReport> list = expenseDao.getExpenseReportsByDate(dateStart, dateEnd);

        if (list == null || list.isEmpty()) {
            logger.info("No reports found between " + dateStart + " and " + dateEnd);
            System.out.println("Could not find any expense reports between " + dateStart + " and " + dateEnd);
            return;
        }

        logger.info("Found " + list.size() + " reports between " + dateStart + " and " + dateEnd);

        System.out.printf(
                "%-5s %-8s %-10s %-40s %-12s %-10s %-10s %-40s %-12s%n",
                "ID",
                "UserID",
                "Amount",
                "Description",
                "Date",
                "Status",
                "Reviewer",
                "Comment",
                "ReviewDate"
        );

        System.out.println(
                "-----------------------------------------------------------------------------------------------------------------------------------------"
        );

        // ROWS
        for (CategoryReport cr : list) {
            System.out.printf(
                    "%-5d %-8d %-10.2f %-40s %-12s %-10s %-10d %-40s %-12s%n",
                    cr.getId(),
                    cr.getUser_id(),
                    cr.getAmount(),
                    cr.getDescription().trim(),
                    cr.getDate(),
                    cr.getStatus(),
                    cr.getReviewer(),
                    cr.getComment(),
                    cr.getReview_date()
            );
        }
    }

}

