package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    void addTaskInHistory(Task task);

    List<Task> getHistory();
}
