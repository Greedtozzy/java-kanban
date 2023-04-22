package service;


import java.io.File;

public class Managers {
    /** Метод, возвращающий экземпляр базового менеджера*/
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /** Метод, возвращающий экземпляр менеджера, который будет записываться в файл.*/
    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager(new File("resources/recordedTasks.csv"));
    }

    /** Метод, возвращающий экземпляр HistoryManager*/
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /** Метод, возвращающий экземпляр менеджера загруженного из файла*/
    public static TaskManager loadFromFile(File file) {
        return new FileBackedTasksManager(file).loadFromFile(file);
    }
}
