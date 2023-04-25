package service;

import exceptions.ManagerSaveException;
import exceptions.TaskCreatingException;
import model.*;

import java.io.*;
import java.nio.file.Files;

import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager loadFromFileTaskManager = new FileBackedTasksManager(file);
        try {
            String[] content = Files.readString(file.toPath()).split(System.lineSeparator());
            List<Integer> ids = new ArrayList<>();
            for (int i = 1; i < content.length - 2; i++) {
                switch (CSVTaskFormat.fromString(content[i]).getType()) {
                    case TASK:
                        Task task = CSVTaskFormat.fromString(content[i]);
                        loadFromFileTaskManager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        Epic epic = (Epic) CSVTaskFormat.fromString(content[i]);
                        loadFromFileTaskManager.epics.put(epic.getId(), epic);
                        loadFromFileTaskManager.checkEpicStatus(epic.getId());
                        break;
                    case SUBTASK:
                        SubTask subTask = (SubTask) CSVTaskFormat.fromString(content[i]);
                        loadFromFileTaskManager.subTasks.put(subTask.getId(), subTask);
                        break;
                }
                ids.add(CSVTaskFormat.fromString(content[i]).getId());
            }
            List<Integer> history = new ArrayList<>();
            for (int i = 1; i < content.length; i++) {
                String line = content[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(content[i + 1]);
                    break;
                }
            }
                for (Integer taskId : history) {
                    loadFromFileTaskManager.historyManager.addTaskInHistory(loadFromFileTaskManager.findTask(taskId));
                }
            loadFromFileTaskManager.id = Collections.max(ids) + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("IOException в методе, восстанавливающем экземпляр менеджера из файла.");
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
        allTasks. putAll(subTasks);
        allTasks.putAll(epics);
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
                fileWriter.write("id,type,name,status,description,startTime,duration,epic");
                fileWriter.newLine();
            for (Task task : allTasks.values()) {
                    fileWriter.write(CSVTaskFormat.taskToString(task) + System.lineSeparator());
            }
            if (!getHistory().isEmpty()) {
                    fileWriter.write(System.lineSeparator() + CSVTaskFormat.historyToString(getHistory()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("IOException в методе save");
        }
    }

    @Override
    public int createNewTask(Task task) {
        int taskId = super.createNewTask(task);
        save();
        return taskId;
    }
    @Override
    public int createNewEpic(Epic epic) {
        int epicId = super.createNewEpic(epic);
        save();
        return epicId;
    }
    @Override
    public int createNewSubTask(SubTask subTask) {
        int subTaskId = super.createNewSubTask(subTask);
        save();
        return subTaskId;
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
        if (tasks.get(id) != null) {
            Task task = tasks.get(id);
            historyManager.addTaskInHistory(task);
            save();
            return task;
        } else {
            System.out.println("Нет задачи с таким id");
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            historyManager.addTaskInHistory(epic);
            save();
            return epic;
        } else {
            System.out.println("Нет эпика с таким id");
            return null;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.get(id) != null) {
            SubTask subTask = subTasks.get(id);
            historyManager.addTaskInHistory(subTask);
            save();
            return subTask;
        } else {
            System.out.println("Нет подзадачи с таким id");
            return null;
        }
    }
}
