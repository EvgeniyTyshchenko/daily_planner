package tasks;

import java.time.LocalDateTime;

public class OneTimeTask extends Task {
    public OneTimeTask(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime) {
        super(heading, description, isWorkTask, localDateTime);
    }

    @Override
    public String getTaskRecurrence() {
        return "Однократная";
    }
}