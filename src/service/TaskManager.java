package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1; // id задачи начинает отсчет от 1

    HashMap<Integer, Task> tasks = new HashMap<>(); // Хранение задач
    HashMap<Integer, Epic> epics = new HashMap<>(); // Хранение эпиков
    HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Хранение подзадач

    public void createNewTask(Task task) {
        tasks.put(id, task);
        task.setId(id++);
    }
    // Метод, создающий новую задачу

    public void createNewEpic(Epic epic) {
        epics.put(id, epic);
        epic.setId(id++);
    }
    // Метод, создающий новый эпик

    public void createNewSubTask(SubTask subTask, int epicId) {
        if (epics.containsKey(epicId)) {
            subTasks.put(id, subTask);
            subTask.setEpicId(epicId);
            epics.get(epicId).addSubTaskId(id);
            subTask.setId(id++);
            checkEpicStatus(epicId);
        } else {
            System.out.println("Вы пытаетесь добавить подзадачу к не существующему эпику. Проверьте epicId.");
        }
    }
    // Метод, создающий новую подзадачу

    public void deleteAllTasks() {
        tasks.clear();
    }
    // Метод, удаляющий все задачи

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }
    // Метод, удаляющий все эпики. Вместе с ними удаляет все подзадачи.

    public void deleteAllSubTasks() {
        for (int epicId : epics.keySet()) {
            epics.get(epicId).subTasksIds.clear();
            checkEpicStatus(epicId);
        }
        subTasks.clear();
    }
    // Метод, удаляющий все подзадачи. Так-же очищяет все списки подзадач во всех эпиках.

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }
    // Метод, удаляющий задачу по требуемуму id

    public void deleteEpicById(int epicId) {
        for (int subTaskId : epics.get(epicId).getSubTasksIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(epicId);

    }
    /* Метод, удаляющий эпик по требуемуму id.
    Так-же удаляет все подзадачи, связавнные с эпиком
     */

    public void deleteSubTaskById(int subTaskId) {
        int epicId = subTasks.get(subTaskId).getEpicId();
        int index = epics.get(epicId).subTasksIds.indexOf(subTaskId);
        epics.get(epicId).subTasksIds.remove(index);
        subTasks.remove(subTaskId);
        checkEpicStatus(epicId);
    }
    /* Метод, удаляющий подзадачу по требуемуму id.
    Так-же удаляет id подзадачи из коллекции в эпике, к которому пренадлежала задача
    */

    public void updateTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, task);
            tasks.get(taskId).setId(taskId);
        } else {
            System.out.println("Нет задачи с таким id.");
        }
    }
    // Метод, обновляющий задачу. По сути просто заменяет в мапе одну задачу на другую.

    public void updateEpic(Epic epic, int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subTasksIds = epics.get(epicId).subTasksIds;
            epics.put(epicId, epic);
            epics.get(epicId).subTasksIds = subTasksIds;
            checkEpicStatus(epicId);
            epics.get(epicId).setId(epicId);
        } else {
            System.out.println("Нет эпика с таким id");
        }
    }
    /* Метод, обновляющий эпик. Принцип тот-же, что и с задачей.
     Но добавлен перенос в новый эпик списка подзадач из старого.
     */

    public void updateSubTask(SubTask subTask, int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            int epicId = subTasks.get(subTaskId).getEpicId();
            subTasks.put(subTaskId, subTask);
            checkEpicStatus(epicId);
            subTasks.get(subTaskId).setId(subTaskId);
        } else {
            System.out.println("Нет подзадачи с таким id");
        }
    }
    // Метод, обновляющий подзадачу.

    public void getListAllTasks() {
        for (int id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
    }
    // Метод, выводящий список всех задач.

    public void getListAllEpics() {
        for (int id : epics.keySet()) {
            System.out.println(epics.get(id));
        }
    }
    // Метод, выводящий список всех эпиков.

    public void getListAllSubTasks() {
        for (int id : subTasks.keySet()) {
            System.out.println(subTasks.get(id));
        }
    }
    // Метод, выводящий список всех подзадач.

    public void getTaskById(int id) {
        if (tasks.containsKey(id)) {
            System.out.println(tasks.get(id));
        } else {
            System.out.println("Задачи с такам id нет");
        }
    }
    // Метод, выводящий задачу по id.

    public void getEpicById(int id) {
        if (epics.containsKey(id)) {
            System.out.println(epics.get(id));
        } else {
            System.out.println("Эпика с такам id нет");
        }
    }
    // Метод, выводящий эпик по id.

    public void getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            System.out.println(subTasks.get(id));
        } else {
            System.out.println("Подзадачи с такам id нет");
        }
    }
    // Метод, выводящий подзадачу по id.

    public  void getSubTasksByEpicId(int id) {
        if (epics.containsKey(id)) {
            for (int subTaskId : epics.get(id).subTasksIds) {
                System.out.println(subTasks.get(subTaskId));
            }
        } else {
            System.out.println("Эпика с такам id нет");
        }
    }
    // Метод, выводящий список подзадач по id эпика.

    public void checkEpicStatus(int epicId) {
        if (!epics.get(epicId).subTasksIds.isEmpty()) {
            for (int subTaskId : epics.get(epicId).subTasksIds) {
                if (subTasks.get(subTaskId).getStatus().equals("IN_PROGRESS")) {
                    epics.get(epicId).setStatus("IN_PROGRESS");
                    break;
                }
            }
            if (!epics.get(epicId).getStatus().equals("IN_PROGRESS")) {
                for (int subTaskId : epics.get(epicId).subTasksIds) {
                    if (subTasks.get(subTaskId).getStatus().equals("DONE")) {
                        epics.get(epicId).setStatus("DONE");
                    }
                    break;
                }
            }
            if (epics.get(epicId).getStatus().equals("DONE")) {
                for (int subTaskId : epics.get(epicId).subTasksIds) {
                    if (subTasks.get(subTaskId).getStatus().equals("NEW")) {
                        epics.get(epicId).setStatus("IN_PROGRESS");
                        break;
                    }
                }
            } else {
                epics.get(epicId).setStatus("NEW");
            }
        } else {
            epics.get(epicId).setStatus("NEW");
        }
    }
    // Страшная консрукция, которая проверяет и меняет статус у эпика.
    // Можно было конечно попробовать enum, но наставник посоветовал отложить это до следующего спринта =)
}
