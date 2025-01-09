package Permanager.utils;

import Permanager.enums.LoggerStatus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LoggerHandler {

    // Объект файла
    FileWriter logFile;

    // Название лог файла
    String logFileName;

    // Путь к лог файлу
    String logFilePath;

    // Формат времени
    private String getTimeFormatter() {
        return "dd-MM-yyyy HH:mm:ss";
    }

    // Конструктор логгера
    public LoggerHandler() {
        LocalDateTime dateNow = LocalDateTime.now();
        this.logFileName = String.format("%s.log", dateNow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.logFilePath = String.format("./src/main/resources/logs/%s-%s/%s", dateNow.getMonthValue(),
                dateNow.getYear(), logFileName);

        try {
            Path path = Path.of(String.format("./src/main/resources/logs/%s-%s", dateNow.getMonthValue(),
                    dateNow.getYear()));

            // Если не существует директории, то создать
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            this.logFile = new FileWriter(this.logFilePath, true);
        } catch (IOException e) {
            System.out.printf("File with name \"%s\" isn't open\n", logFileName);
        }
    }

    public void writeLog(String message, LoggerStatus status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFilePath, true))) {
            String log = String.format("[%s]\t[%s]\t%s", status.getName(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(getTimeFormatter())), message);

            writer.write(log); // Форматируем и записываем сообщение в файл
            writer.newLine();  // Переход на новую строку
        } catch (IOException err) {
            System.out.printf("Log message isn't write in \"%s\" file\n", logFileName);
        }
    }

    public void writeLog(String message, LoggerStatus status, Boolean inConsole) {
        if (inConsole) {
            System.out.printf(status.getName() + ": %s\n", message);
        }
        writeLog(message, LoggerStatus.INFO);
    }

    public void info(String message) {
        writeLog(message, LoggerStatus.INFO);
    }

    public void info(String message, Boolean inConsole) {
        writeLog(message, LoggerStatus.INFO, inConsole);
    }

    public void debug(String message) {
        writeLog(message, LoggerStatus.DEBUG);
    }

    public void debug(String message, Boolean inConsole) {
        writeLog(message, LoggerStatus.DEBUG, inConsole);
    }

    public void error(String message) {
        writeLog(message, LoggerStatus.ERROR);
    }

    public void error(String message, Boolean inConsole) {
        writeLog(message, LoggerStatus.ERROR, inConsole);
    }

    public void warning(String message) {
        writeLog(message, LoggerStatus.WARNING);
    }

    public void warning(String message, Boolean inConsole) {
        writeLog(message, LoggerStatus.WARNING, inConsole);
    }

    public void trace(String message) {
        writeLog(message, LoggerStatus.TRACE);
    }

    public void trace(String message, Boolean inConsole) {
        writeLog(message, LoggerStatus.TRACE, inConsole);
    }

    public void fatal(String message) {
        writeLog(message, LoggerStatus.FATAL);
    }

    public void fatal(String message, Boolean inConsole) {
        writeLog(message, LoggerStatus.FATAL, inConsole);
    }
}