package tasks;

import java.time.LocalDateTime;

public class DailyTask extends Task implements Repeatable {
    public DailyTask(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime) {
        super(heading, description, isWorkTask, localDateTime);
    }

    @Override
    public String getTaskRecurrence() {
        return "Ежедневная";
    }

    @Override
    public LocalDateTime getNextDateAndTime(LocalDateTime dateTime) {
        LocalDateTime ldt = getLocalDateTime();
        while (ldt.isBefore(dateTime)) {
            ldt = ldt.plusDays(1);
        }
        return ldt;
    }
}