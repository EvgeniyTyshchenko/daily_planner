package tasks;

import java.time.LocalDateTime;

public class WeeklyTask extends Task implements Repeatable {
    public WeeklyTask(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime) {
        super(heading, description, isWorkTask, localDateTime);
    }

    @Override
    public String getTaskRecurrence() {
        return "Еженедельная";
    }

    @Override
    public LocalDateTime getNextDateAndTime(LocalDateTime dateTime) {
        LocalDateTime ldt = getLocalDateTime();
        while (ldt.isBefore(dateTime)) {
            ldt = ldt.plusWeeks(1);
        }
        return ldt;
    }
}