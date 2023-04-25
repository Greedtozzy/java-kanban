package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends model.Task {
    /** Хранение списка подзадач эпика*/
    public List<Integer> subTasksIds = new ArrayList<>();
    private LocalDateTime endTime = null;

    public Epic(String name, String description) {
        super(name, description, null, 0);
        status = TaskStatus.NEW;
    }

    public Epic(String name, String description, int id) {
        super(name, description, TaskStatus.NEW,
                id, null, 0);
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    /** Метод, добавляющий номер подзадачи в список подзадач*/
    public void addSubTaskId(int subTasksId) {
        subTasksIds.add(subTasksId);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "Эпик = " + name + ", " +
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
                durationInMinutes;
    }
}
