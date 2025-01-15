package Permanager;


import Permanager.utils.LoggerHandler;

public class Main {

    public static void main(String[] args) {
        LoggerHandler logger = new LoggerHandler();

        logger.info("----------------------------------------");
        new CommandManager();
    }
}