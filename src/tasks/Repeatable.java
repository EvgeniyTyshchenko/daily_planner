package tasks;

import java.time.LocalDateTime;

public interface Repeatable {
    LocalDateTime getNextDateAndTime(LocalDateTime localDateTime);
}