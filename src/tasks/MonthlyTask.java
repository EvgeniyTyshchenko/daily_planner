package tasks;

import java.time.LocalDateTime;

public class MonthlyTask extends Task implements Repeatable {
    public MonthlyTask(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime) {
        super(heading, description, isWorkTask, localDateTime);
    }

    @Override
    public String getTaskRecurrence() {
        return "Ежемесячная";
    }

    @Override
    public LocalDateTime getNextDateAndTime(LocalDateTime dateTime) {
        LocalDateTime ldt = getLocalDateTime();
        while (ldt.isBefore(dateTime)) {
            ldt = ldt.plusMonths(1);
        }
        return ldt;
    }
}