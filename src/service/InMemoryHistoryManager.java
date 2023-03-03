package service;

import model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    @Override
    public List<Task> getHistory() {
        return taskCallHistory;
    }
    // Метод, выводящий список, в котором хранится история обращений к задачам.

    @Override
    public void addTaskInHistory(Task task) {
        if (taskCallHistory.size() < 10) {
            taskCallHistory.add(task);
        } else {
            taskCallHistory.remove(0);
            taskCallHistory.add(task);
        }
    }
    // Метод, добавляющий задачу в список. Используется после обращения к ней. Так-же проверяет длину списка.
}
