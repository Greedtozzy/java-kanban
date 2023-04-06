package service;

import model.*;

import java.io.*;
import java.nio.file.Files;

import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    static File file;
    static CSVTaskFormat csvTaskFormat = new CSVTaskFormat();

    public FileBackedTasksManager(String path) {
        super();
        this.file = new File(path);
    }

    public static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager loadFromFileTaskManager = new FileBackedTasksManager(path);

        try {
            String[] content = Files.readString(file.toPath()).split(System.lineSeparator());
            List<Integer> ids = new ArrayList<>();
            for (int i = 1; i < content.length - 2; i++) {
                switch (csvTaskFormat.fromString(content[i]).getType()) {
                    case TASK:
                        loadFromFileTaskManager.tasks.put(csvTaskFormat.fromString(content[i]).getId(),
                                csvTaskFormat.fromString(content[i]));
                        break;
                    case EPIC:
                        loadFromFileTaskManager.epics.put(csvTaskFormat.fromString(content[i]).getId(),
                                (Epic) csvTaskFormat.fromString(content[i]));
                        break;
                    case SUBTASK:
                        loadFromFileTaskManager.subTasks.put(csvTaskFormat.fromString(content[i]).getId(),
                                (SubTask) csvTaskFormat.fromString(content[i]));
                        break;
                }
                ids.add(csvTaskFormat.fromString(content[i]).getId());
            }
            for (int id : csvTaskFormat.historyFromString(content[content.length-1])) {
                if (loadFromFileTaskManager.tasks.containsKey(id)) {
                    loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.tasks.get(id));
                } else if (loadFromFileTaskManager.epics.containsKey(id)) {
                    loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.epics.get(id));
                } else if (loadFromFileTaskManager.subTasks.containsKey(id)) {
                    loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.subTasks.get(id));
                }
            }
            loadFromFileTaskManager.id = Collections.max(ids) + 1;
        } catch (IOException e) {
            throw new RuntimeException("IOException в методе, восстанавливающем экземпляр менеджера из файла.", e);
        }
        for (int subTaskId : loadFromFileTaskManager.subTasks.keySet()) {
            int epicId = loadFromFileTaskManager.subTasks.get(subTaskId).getEpicId();
            loadFromFileTaskManager.epics.get(epicId).addSubTaskId(subTaskId);
        }
        return loadFromFileTaskManager;
    }
    // Метод, разворачивающий экземпляр менеджера из файла.

    private void save() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks. putAll(subTasks);
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
                fileWriter.write("id,type,name,status,description,epic" + System.lineSeparator());
            for (Task task : allTasks.values()) {
                    fileWriter.write(csvTaskFormat.taskToString(task) + System.lineSeparator());
            }
            if (!getHistory().isEmpty()) {
                    fileWriter.write(System.lineSeparator() + csvTaskFormat.historyToString(getHistory()));
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException в методе save", e);
        }
    }

//    private String taskToString(Task task) {
//        return task.taskToString();
//    }
//
//    private static Task fromString(String value) {
//        String[] content = value.split(",");
//        try {
//            TaskType taskType = TaskType.valueOf(content[1]);
//            String name = content[2];
//            String description = content[4];
//            TaskStatus status = TaskStatus.valueOf(content[3]);
//            int id = Integer.parseInt(content[0]);
//            if (taskType.equals(TASK)) {
//                return new Task(name, description, status, id);
//            } else if (taskType.equals(EPIC)) {
//                return new Epic(name, description, status, id);
//            } else if (taskType.equals(SUBTASK)) {
//                return new SubTask(name, description, status, id, Integer.parseInt(content[5]));
//            } else {
//                return null;
//            }
//        } catch (NumberFormatException e) {
//            throw new RuntimeException("При восстановлении задачи в качестве id передено не число", e);
//        }
//    }

    @Override
    public int createNewTask(Task task) {
        super.createNewTask(task);
        save();
        return task.getId();
    }
    @Override
    public int createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
        return epic.getId();
    }
    @Override
    public int createNewSubTask(SubTask subTask) {
        super.createNewSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubTaskById(int subTaskId) {
        super.deleteSubTaskById(subTaskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addTaskInHistory(task);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addTaskInHistory(epic);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.addTaskInHistory(subTask);
        save();
        return subTask;
    }
}
