package service;

import model.Epic;
import model.SubTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static service.FileBackedTasksManager.fromString;
import static service.FileBackedTasksManager.historyFromString;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static TaskManager getFileBacked() {
        return new FileBackedTasksManager(Path.of("resources/recordedTasks.csv"));
    }
    // Метод, возвращиющий экземпляр менеджера, который будет записываться в файл.

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager loadFromFileTaskManager = new FileBackedTasksManager(path);
        String[] content = new String[0];
        try {
            content = Files.readString(path).split("\n");
        } catch (IOException e) {
            throw new RuntimeException("IOException в методе, восстанавливающем экземпляр менеджера из файла.", e);
        }
        List<Integer> ids = new ArrayList<>();
        for (int i = 1; i < content.length - 2; i++) {

            switch (fromString(content[i]).getType()) {
                case TASK:
                    loadFromFileTaskManager.tasks.put(fromString(content[i]).getId(),
                            fromString(content[i]));
                    break;
                case EPIC:
                    loadFromFileTaskManager.epics.put(fromString(content[i]).getId(),
                            (Epic) fromString(content[i]));
                    break;
                case SUBTASK:
                    loadFromFileTaskManager.subTasks.put(fromString(content[i]).getId(),
                            (SubTask) fromString(content[i]));
                    break;
            }
            ids.add(fromString(content[i]).getId());
        }
        for (int id : historyFromString(content[content.length-1])) {
            if (loadFromFileTaskManager.tasks.containsKey(id)) {
                loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.tasks.get(id));
            } else if (loadFromFileTaskManager.epics.containsKey(id)) {
                loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.epics.get(id));
            } else if (loadFromFileTaskManager.subTasks.containsKey(id)) {
                loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.subTasks.get(id));
            }
        }
        loadFromFileTaskManager.id = Collections.max(ids) + 1;
        return loadFromFileTaskManager;
    }
    // Метод, разворачивающий экземпляр менеджера из файла.
}
