package model;

import java.time.LocalDateTime;

public class SubTask extends model.Task {
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
}
