package com.revature.dao;

import com.revature.model.CategoryReport;
import com.revature.model.ExpenseReport;

import java.util.List;

public interface ExpenseReportDAO {
    //As a manager, I want to view a list of all pending expenses so that I can review them efficiently.
    public List<ExpenseReport> getPendingExpenseReports();

    //As a manager, I want to approve or deny submitted expenses so that I can manage reimbursements appropriately.
    public boolean updateStatus(int id, String status, int  reviewer_id);

    //As a manager, I want to add comments to expense decisions so that employees understand the reasoning behind approvals or denials.
    public void updateComment(int id, String comment);

    //As a manager, I want to generate reports by employee, category, or date so that I can analyze spending trends and make informed decisions.
    public List<CategoryReport> getExpenseReportByUsername(String username);
    public List<CategoryReport> getExpenseReportsByCategory(String category);
    public List<CategoryReport> getExpenseReportsByDate(String startDate, String endDate);

}




