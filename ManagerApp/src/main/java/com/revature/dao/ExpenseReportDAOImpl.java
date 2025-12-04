package com.revature.dao;

import com.revature.model.Approvals;
import com.revature.model.CategoryReport;
import com.revature.model.ExpenseReport;
import java.time.LocalDate;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ExpenseReportDAOImpl implements ExpenseReportDAO{

    // connect to DB
    private static final String DB_URL;
    static {
        String baseDir = System.getProperty("user.dir");
        Path dbPath = Paths.get(baseDir, "..", "revature_expense.db").toAbsolutePath().normalize();
        DB_URL = "jdbc:sqlite:" + dbPath.toString();
    }

    public List<ExpenseReport> getPendingExpenseReports(){

        List<Integer> expenseIds = new ArrayList<>();
        List<ExpenseReport> expenseReports = new ArrayList<>();

        // approvals query
        String approvalQuery = "SELECT * FROM approvals WHERE status = 'pending'";
        String expenseQuery = "SELECT * FROM expense_reports WHERE id = ?";

        // approval attempt
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(approvalQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                expenseIds.add(rs.getInt("expense_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return early if no pending reports found
        if (expenseIds.isEmpty()) {
            System.out.println("No pending approvals.");
            return null;
        }

        // expense_report attempt
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(expenseQuery)) {

            for (int expenseId : expenseIds) {

                pstmt.setInt(1, expenseId);
                try (ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        ExpenseReport report = new ExpenseReport();
                        report.setId(rs.getInt("id"));
                        report.setUser_id(rs.getInt("user_id"));
                        report.setDescription(rs.getString("description"));
                        report.setAmount(rs.getDouble("amount"));
                        report.setDate(rs.getString("date")); // temp
                        expenseReports.add(report);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenseReports;
    }

    public boolean updateStatus(int id, String status, int reviewer_id) {
        String statusQuery = "UPDATE approvals SET status = ?, reviewer = ?, review_date = ? WHERE expense_id = ?";

        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.toString();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(statusQuery)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, reviewer_id);
            pstmt.setString(3, dateString);
            pstmt.setInt(4, id);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateComment(int id, String comment){

        String updateQuery = "UPDATE approvals SET comment = ? WHERE expense_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, comment);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not update records comment");
            e.printStackTrace();
        }
    }

    public List<CategoryReport> getExpenseReportByUsername(String username){
        List<CategoryReport> categoryReports = new ArrayList<>();

        // find user id from username
        String userQuery = "SELECT id FROM user_info WHERE username = ? LIMIT 1";
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(userQuery)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (userId == -1) {
            System.out.println("Could not find user with username: " + username);
            return categoryReports;
        } else{
            System.out.println("Found user id: " + userId);
        }

        String byUsernameQuery =
                "SELECT " +
                        "er.id, er.user_id, er.amount, er.description, er.date, " +
                        "a.status, a.comment, a.reviewer, a.review_date " +
                        "FROM expense_reports er " +
                        "LEFT JOIN approvals a ON er.id = a.expense_id " +
                        "WHERE er.user_id = ?";


        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(byUsernameQuery)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ExpenseReport report = new ExpenseReport();
                report.setId(rs.getInt("id"));
                report.setUser_id(rs.getInt("user_id"));
                report.setDescription(rs.getString("description"));
                report.setAmount(rs.getDouble("amount"));
                report.setDate(rs.getString("date"));

                Approvals approval = new Approvals();
                approval.setStatus(rs.getString("status"));
                approval.setComment(rs.getString("comment"));
                approval.setReviewer(rs.getInt("reviewer"));
                approval.setReview_date(rs.getString("review_date"));

                CategoryReport cr = new CategoryReport(report, approval);
                categoryReports.add(cr);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryReports;
    }

public List<CategoryReport> getExpenseReportsByCategory(String category) {
    List<CategoryReport> categoryReports = new ArrayList<>();

    String query =
            "SELECT " +
                    "er.id, er.user_id, er.amount, er.description, er.date, " +
                    "a.status, a.comment, a.reviewer, a.review_date " +
                    "FROM expense_reports er " +
                    "LEFT JOIN approvals a ON er.id = a.expense_id " +
                    "WHERE a.status = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, category);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {

            // Build ExpenseReport
            ExpenseReport report = new ExpenseReport();
            report.setId(rs.getInt("id"));
            report.setUser_id(rs.getInt("user_id"));
            report.setAmount(rs.getDouble("amount"));
            report.setDescription(rs.getString("description"));
            report.setDate(rs.getString("date"));

            // Build Approvals
            Approvals approval = new Approvals();
            approval.setStatus(rs.getString("status"));
            approval.setComment(rs.getString("comment"));
            approval.setReviewer(rs.getInt("reviewer"));
            approval.setReview_date(rs.getString("review_date"));

            // Combine into CategoryReport
            CategoryReport cr = new CategoryReport(report, approval);
            categoryReports.add(cr);
        }

    } catch (SQLException e) {
        System.out.println("Could not find expense reports by category.");
        e.printStackTrace();
    }

    return categoryReports;
}

public List<CategoryReport> getExpenseReportsByDate(String startDate, String endDate) {
    List<CategoryReport> categoryReports = new ArrayList<>();

    String query =
            "SELECT " +
                    "er.id, er.user_id, er.amount, er.description, er.date, " +
                    "a.status, a.comment, a.reviewer, a.review_date " +
                    "FROM expense_reports er " +
                    "LEFT JOIN approvals a ON er.id = a.expense_id " +
                    "WHERE er.date BETWEEN ? AND ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, startDate);
        pstmt.setString(2, endDate);

        System.out.println("Executing date search: " + startDate + " → " + endDate);

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {

            ExpenseReport report = new ExpenseReport();
            report.setId(rs.getInt("id"));
            report.setUser_id(rs.getInt("user_id"));
            report.setDescription(rs.getString("description"));
            report.setAmount(rs.getDouble("amount"));
            report.setDate(rs.getString("date"));

            Approvals approval = new Approvals();
            approval.setStatus(rs.getString("status"));
            approval.setComment(rs.getString("comment"));
            approval.setReviewer(rs.getInt("reviewer"));
            approval.setReview_date(rs.getString("review_date"));

            CategoryReport cr = new CategoryReport(report, approval);
            categoryReports.add(cr);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return categoryReports;
}

}
