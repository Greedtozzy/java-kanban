package service;


import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager(new File("resources/recordedTasks.csv"));
    }
    // Метод, возвращиющий экземпляр менеджера, который будет записываться в файл.

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager loadFromFile(File file) {
        return new FileBackedTasksManager(file).loadFromFile(file);
    }
}
