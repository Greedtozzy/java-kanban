package model;

public class Task {
    protected String name;
    protected String description;
    protected String status;
    protected int id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id = " + id + ", " +
                "Задача = " + name + ", " +
                "Описание = " + description + ", " +
                "Статус = " + status;
    }
    /* Переписал переопределение toString, чтобы список был максимально информативный и читаемый.
    Так-же отдельно переопределил его в наследниках, что-бы поля назывались верно.
    */
}
