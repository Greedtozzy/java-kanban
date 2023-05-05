package service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URI;

public class Managers {
    /** Метод, возвращающий экземпляр базового менеджера*/
    public static TaskManager getDefault()   {
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
        return new FileBackedTasksManager(file);
    }

    /** Метод, возвращающий экземпляр класса Gson*/
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    /***/
    public static TaskManager getHttpTaskManager() {
        return new HttpTaskManager(URI.create("http://localhost:8080"));
    }
}
