package service;


public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager("resources/recordedTasks.csv");
    }
    // Метод, возвращиющий экземпляр менеджера, который будет записываться в файл.

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager loadFromFile(String path) {
        return new FileBackedTasksManager(path).loadFromFile(path);
    }
}
