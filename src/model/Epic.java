package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Integer> subTasksIds = new ArrayList<>(); // Хранение списка подзадач эпика

    public Epic(String name, String description, TaskStatus status, int id, List<Integer> subTasksIds) {
        super(name, description, status, id);
        this.subTasksIds = subTasksIds;
    }

    public Epic(String name, String description, TaskStatus status, int id) {
        super(name, description, status, id);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTaskId(int subTasksId) {
        subTasksIds.add(subTasksId);
    } // Метод, добавляющий номер подзадачи в список подзадач

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
        return id + "," + getType() + "," + name + "," + status + "," + description;
    }
}
