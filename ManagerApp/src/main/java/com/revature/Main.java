package com.revature;

import com.revature.ui.Menu;

public class Main {

    // main -> service -> dao -> db -> dao -> model -> service (prints out expense_reports of specified user) -> main
    // can only call services from main and nothing else.

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.showMenu();
    }
}