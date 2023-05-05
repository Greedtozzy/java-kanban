package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
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
                "Статус = " + status + "," +
                "Время начала = " + startTime + "," +
                "Длительность = " + durationInMinutes + "," +
                "Окончание = " + endTime +
                "Подзадачи = " + subTasksIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksIds, epic.subTasksIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds, endTime);
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        Epic epic = (Epic) o;
//        return subTasksIds.equals(epic.subTasksIds) && endTime == epic.endTime;
//    }
//
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), subTasksIds, endTime);
//    }

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
