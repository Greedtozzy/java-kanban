package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> taskCallHistory = new ArrayList<>();
    @Override
    public List<Task> getHistory() {
        return taskCallHistory;
    }
    // Метод, выводящий список, в котором хранится история обращений к задачам.

    @Override
    public void addTaskInHistory(Task task) {
        if (task != null) {
            if (taskCallHistory.size() > 10) {
                taskCallHistory.remove(0);
            }
            taskCallHistory.add(task);
        }
    }
    // Метод, добавляющий задачу в список. Используется после обращения к ней. Так-же проверяет длину списка.
}
