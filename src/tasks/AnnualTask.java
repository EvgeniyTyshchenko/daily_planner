package tasks;

import java.time.LocalDateTime;

public class AnnualTask extends Task implements Repeatable {
    public AnnualTask(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime) {
        super(heading, description, isWorkTask, localDateTime);
    }

    @Override
    public String getTaskRecurrence() {
        return "Ежегодная";
    }

    @Override
    public LocalDateTime getNextDateAndTime(LocalDateTime dateTime) {
        LocalDateTime ldt = getLocalDateTime();
        while (ldt.isBefore(dateTime)) {
            ldt = ldt.plusYears(1);
        }
        return ldt;
    }
}