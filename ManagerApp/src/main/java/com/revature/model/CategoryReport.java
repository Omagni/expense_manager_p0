package com.revature.model;

public class CategoryReport {


    int id; // expense report id
    int user_id; // user id
    double amount; // expense report amount
    String description; // expense report desc
    String date; // expense report date
    String status; // approvals status
    int reviewer; // approvals review
    String comment; // approvals comment
    String review_date; // approvals reivew_date

    public CategoryReport(ExpenseReport report, Approvals approval) {
        this.id = report.getId();
        this.user_id = report.getUser_id();
        this.amount = report.getAmount();
        this.description = report.getDescription();
        this.date = report.getDate();
        this.status = approval.getStatus();
        this.reviewer = approval.getReviewer();
        this.comment = approval.getComment();
        this.review_date = approval.getReview_date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReviewer() {
        return reviewer;
    }

    public void setReviewer(int reviewer) {
        this.reviewer = reviewer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    @Override
    public String toString() {
        return "CategoryReport{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", reviewer=" + reviewer +
                ", comment='" + comment + '\'' +
                ", review_date='" + review_date + '\'' +
                '}';
    }
}
