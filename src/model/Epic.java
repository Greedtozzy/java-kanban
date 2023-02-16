package model;

import java.util.ArrayList;

public class Epic extends Task {

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }
    public ArrayList<Integer> subTasksIds = new ArrayList<>(); // Хранение списка подзадач эпика

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTaskId(int subTasksId) {
        subTasksIds.add(subTasksId);
    } // Метод, добавляющий номер подзадачи в список подзадач

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "Эпик = " + name + ", " +
                "Описание = " + description + ", " +
                "Статус = " + status;
    }
}