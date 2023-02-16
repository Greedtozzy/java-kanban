package model;

public class SubTask extends Task {
    public SubTask(String name, String description, String status) {
        super(name, description, status);
    }
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "Подзадача = " + name + ", " +
                "Описание = " + description + ", " +
                "Статус = " + status;
    }
}
