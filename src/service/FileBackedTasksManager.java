package service;

import model.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    Path path;
    Writer fileWriter;

    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
        try {
            this.fileWriter = new FileWriter(String.valueOf(path), true);
        } catch (IOException e) {
            throw new RuntimeException("IOException в конструкторе", e);
        }
    }

    private void save() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks. putAll(subTasks);
        try (Writer fileWriter = new FileWriter(String.valueOf(path))) {
                fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : allTasks.values()) {
                    fileWriter.write(taskToString(task) + "\n");
            }
            if (!getHistory().isEmpty()) {
                    fileWriter.write("\n" + historyToString(getHistory()));
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException в методе save", e);
        }
    }

    public String taskToString(Task task) {
        return task.taskToString();
    }

    public static Task fromString(String value) {
        String[] content = value.split(",");
        try {
            TaskType taskType = TaskType.valueOf(content[1]);
            String name = content[2];
            String description = content[4];
            TaskStatus status = TaskStatus.valueOf(content[3]);
            int id = Integer.parseInt(content[0]);
            if (taskType.equals(TASK)) {
                return new Task(name, description, status, id);
            } else if (taskType.equals(EPIC)) {
                return new Epic(name, description, status, id);
            } else if (taskType.equals(SUBTASK)) {
                return new SubTask(name, description, status, id, Integer.parseInt(content[5]));
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("При восстановлении задачи в качестве id передено не число", e);
        }
    }

    public String historyToString(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.getId() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] content = value.split(",");
        for (String i : content) {
            try {
                history.add(Integer.parseInt(i));
            } catch (NumberFormatException e) {
                throw new RuntimeException("При восстановлении истории задач в качестве id передано не число", e);
            }
        }
        return history;
    }

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
