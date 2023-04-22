package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static model.TaskType.*;

public class CSVTaskFormat {

    public static String taskToString(Task task) {
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
                return new Epic(name, description, id);
            } else if (taskType.equals(SUBTASK)) {
                return new SubTask(name, description, status, id, Integer.parseInt(content[5]));
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("При восстановлении задачи в качестве id передено не число", e);
        }
    }

    public static String historyToString(List<Task> tasks) {
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
                e.getCause();
            }
        }
        return history;
    }
}
