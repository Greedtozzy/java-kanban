package model;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status,int id, int epicId) {
        super(name, description, status, id);
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
        return id + "," + getType() + "," + name + "," + status + "," + description + "," + epicId;
    }
}
