package tasks;

import utility.ValidateUtil;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Task {
    private String heading;
    private String description;
    private boolean isWorkTask;
    private LocalDateTime localDateTime;
    private static int counter = 1;
    private final int id;
    private boolean remoteTask;

    public Task(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime) {
        this.heading = ValidateUtil.validateString(heading);
        this.description = ValidateUtil.validateString(description);
        this.isWorkTask = isWorkTask;
        this.localDateTime = localDateTime;
        this.remoteTask = true;
        id = counter;
        counter ++;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = ValidateUtil.validateString(heading);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = ValidateUtil.validateString(description);
    }

    public boolean getIsWorkTask() {
        return isWorkTask;
    }

    public void setIsWorkTask(boolean isWorkTask) {
        this.isWorkTask = isWorkTask;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getId() {
        return id;
    }

    public void setRemoteTask(boolean remoteTask) {
        this.remoteTask = remoteTask;
    }

    public abstract String getTaskRecurrence();

    @Override
    public String toString() {
        String isWork;
        if (isWorkTask) {
            isWork = "Рабочая задача";
        } else {
            isWork = "Личная задача";
        }
        return "[Задача " + getId() + "] " +
                getTaskRecurrence() + "\n" +
                "Название: " + getHeading() + ";\n" +
                "Описание: " + getDescription() + ";\n" +
                "Тип: " + isWork + ";\n" +
                "Дата и время начала выполнения: " + getLocalDateTime() + ";\n" +
                "< активность задачи: " + remoteTask + " >\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return isWorkTask == task.isWorkTask && id == task.id && remoteTask == task.remoteTask && Objects.equals(heading, task.heading) && Objects.equals(description, task.description) && Objects.equals(localDateTime, task.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, description, isWorkTask, localDateTime, id, remoteTask);
    }
}