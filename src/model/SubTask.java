package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name,
                   String description,
                   int epicId,
                   LocalDateTime startTime,
                   long duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        this.status = TaskStatus.NEW;
    }

    public SubTask(String name,
                   String description,
                   TaskStatus status,
                   int id,
                   int epicId,
                   LocalDateTime startTime,
                   long durationInMinutes) {
        super(name, description, status, id, startTime, durationInMinutes);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "Подзадача = " + name + ", " +
                "Описание = " + description + ", " +
                "Статус = " + status;
    }

    @Override
    public String taskToString() {
        return id + "," +
                getType() + "," +
                name + "," +
                status + "," +
                description + "," +
                startTime + "," +
                durationInMinutes + "," +
                epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
