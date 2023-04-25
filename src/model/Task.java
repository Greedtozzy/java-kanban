package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected int id;
    protected LocalDateTime startTime = null;
    protected long durationInMinutes = 0;

    public Task(String name,
                String description,
                LocalDateTime startTime,
                long durationInMinutes) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.durationInMinutes = durationInMinutes;
        this.status = TaskStatus.NEW;
    }

    public Task(String name,
                String description,
                TaskStatus status,
                int id,
                LocalDateTime startTime,
                long durationInMinutes) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.durationInMinutes = durationInMinutes;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(durationInMinutes);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "Задача = " + name + ", " +
                "Описание = " + description + ", " +
                "Статус = " + status + "," +
                "Время начала = " + startTime + "," +
                "Длительность = " + durationInMinutes;
    }

    public String taskToString() {
        return id + "," +
                getType() + "," +
                name + "," +
                status + "," +
                description + "," +
                startTime + "," +
                durationInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && durationInMinutes == task.durationInMinutes && name.equals(task.name) && description.equals(task.description) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, startTime, durationInMinutes);
    }


    @Override
    public int compareTo(Task o) {
        if (getStartTime() == null) {
            return 1;
        } else if (o.getStartTime() == null) {
            return -1;
        } else {
            if (getStartTime().isAfter(o.getStartTime())) {
                return 1;
            } else if (getStartTime().isBefore(o.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
