package com.revature.service;

import com.revature.model.ExpenseReport;

import java.util.List;

public interface ManageService {

    // view all pending expenses
    public void viewPendingExpenses();

    // approve or deny a submitted expense
    public void updateStatus(int reviewer_id);

    // add comments to an expense decision
    public void updateComment();

    // generate reports by employee, category, or date
    public void generateReportsByType();

    public void getExpenseReportByUsername();
    public void getExpenseReportsByCategory();
    public void getExpenseReportsByDate();

}
