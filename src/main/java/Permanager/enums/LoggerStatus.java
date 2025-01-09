package Permanager.enums;

public enum LoggerStatus {
    INFO("INFO"),
    DEBUG("DEBUG"),
    ERROR("ERROR"),
    WARNING("WARN"),
    FATAL("FATAL"),
    TRACE("TRACE");

    private final String status;

    LoggerStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return status;
    }
}
