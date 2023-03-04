package service;

import model.Epic;
import model.SubTask;
import model.Task;

public class Test {
//    TaskManager taskManager = Managers.getDefault();
//    HistoryManager historyManager = Managers.getDefaultHistory();
    public void testSprint4(TaskManager taskManager, HistoryManager historyManager) {
        Task task = new Task("taskName", "taskDescription");
        taskManager.createNewTask(task);
        System.out.println(task);

        Epic epic = new Epic("epicName", "epicDescription");
        taskManager.createNewEpic(epic);
        System.out.println(epic);

        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic.getId());
        taskManager.createNewSubTask(subTask);
        System.out.println(subTask);

        taskManager.getTaskById(1);
        System.out.println(taskManager.getTaskById(1));

        taskManager.getEpicById(2);
        System.out.println(taskManager.getEpicById(2));

        taskManager.getSubTaskById(3);
        System.out.println(taskManager.getSubTaskById(3));

        taskManager.getHistory();
        System.out.println(taskManager.getHistory());
    }
}
