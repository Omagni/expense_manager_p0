package com.revature;

import com.revature.ui.Menu;

import java.io.IOException;
import java.util.logging.*;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // main -> service -> dao -> db -> dao -> model -> service (prints out expense_reports of specified user) -> main
    // can only call services from main and nothing else.

    public static void main(String[] args) {
        setupLogger();

        logger.info("Application started.");

        Menu menu = new Menu();
        menu.showMenu();
    }

    private static void setupLogger() {
        try {
            Logger rootLogger = Logger.getLogger("");

            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }

            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);

            rootLogger.setLevel(Level.INFO);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
