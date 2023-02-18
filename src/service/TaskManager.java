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

    public void createNewSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTasks.put(id, subTask);
            epics.get(subTask.getEpicId()).addSubTaskId(id);
            subTask.setId(id++);
            checkEpicStatus(subTask.getEpicId());
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
        for (Epic epic : epics.values()) {
            epic.subTasksIds.clear();
            checkEpicStatus(epic.getId());
        }
        subTasks.clear();
    }
    // Метод, удаляющий все подзадачи. Так-же очищяет все списки подзадач во всех эпиках.

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }
    // Метод, удаляющий задачу по требуемуму id

    public void deleteEpicById(int epicId) {
        Epic epic = epics.remove(epicId);
        if(epic !=null){
            for (Integer subtaskId : epic.getSubTasksIds()) {
                subTasks.remove(subtaskId);
            }
        }
    }
    /* Метод, удаляющий эпик по требуемуму id.
    Так-же удаляет все подзадачи, связавнные с эпиком
     */

    public void deleteSubTaskById(int subTaskId) {
        SubTask subTask = subTasks.remove(subTaskId);
        if (subTask != null) {
            int epicId = subTasks.get(subTaskId).getEpicId();
            epics.get(epicId).subTasksIds.remove((Integer) subTaskId);
            checkEpicStatus(epicId);
        }
    }
    /* Метод, удаляющий подзадачу по требуемуму id.
    Так-же удаляет id подзадачи из коллекции в эпике, к которому пренадлежала задача
    */

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Нет задачи с таким id.");
        }
    }
    // Метод, обновляющий задачу. По сути просто заменяет в мапе одну задачу на другую.

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            ArrayList<Integer> subTasksIds = epics.get(epic.getId()).subTasksIds;
            epics.put(epic.getId(), epic);
            epics.get(epic.getId()).subTasksIds = subTasksIds;
            checkEpicStatus(epic.getId());
        } else {
            System.out.println("Нет эпика с таким id");
        }
    }
    /* Метод, обновляющий эпик. Принцип тот-же, что и с задачей.
     Но добавлен перенос в новый эпик списка подзадач из старого.
     */

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            int epicId = subTasks.get(subTask.getId()).getEpicId();
            if (epics.containsKey(subTask.getEpicId())) {
                subTasks.put(subTask.getId(), subTask);
                checkEpicStatus(epicId);
                subTasks.get(subTask.getId()).setId(subTask.getId());
            } else {
                System.out.println("Нет эпика, к которому привязана эта подзадача");
            }
        } else {
            System.out.println("Нет подзадачи с таким id");
        }
    }
    // Метод, обновляющий подзадачу.

    public ArrayList getListAllTasks() {
        ArrayList<Task> allTasks = new ArrayList();
        for (Task task : tasks.values()) {
            allTasks.add(task);
        }
        return allTasks;
    }
    // Метод, выводящий список всех задач.

    public ArrayList getListAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }
        return allEpics;
    }
    // Метод, выводящий список всех эпиков.

    public ArrayList getListAllSubTasks() {
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTasks.get(id));
        }
        return allSubTasks;
    }
    // Метод, выводящий список всех подзадач.

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        return task;
    }
    /* Метод, выводящий задачу по id.
    Здесь может вернуть null, если по id передать не существующую задачу. Дальше то-же самое.
    Проверку можно провести при выводе в консоли (перед передачей в front).
    Наставник склазал, что здесь нужно инсключение бросать, но это мы будем проходить позже.
     */

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        return epic;
    }
    // Метод, выводящий эпик по id.

    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        return subTask;
    }
    // Метод, выводящий подзадачу по id.

    public ArrayList getSubTasksByEpicId(int id) {
        ArrayList<SubTask> subTasksByEpic = new ArrayList<>();
        for (int subTaskId : epics.get(id).subTasksIds) {
            subTasksByEpic.add(subTasks.get(subTaskId));
        }
        return subTasksByEpic;
    }
    // Метод, выводящий список подзадач по id эпика.

    public void checkEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epic.subTasksIds.isEmpty()) {
            String status = null;
            for (int id : epic.getSubTasksIds()) {
                final SubTask subtask = subTasks.get(id);
                if (status == null) {
                    status = subtask.getStatus();
                    continue;
                }
                if (status.equals(subtask.getStatus())
                        && !status.equals("IN_PROGRESS")) {
                    continue;
                }
                epic.setStatus("IN_PROGRESS");
                return;
            }
            epic.setStatus(status);
//            for (int subTaskId : epics.get(epicId).subTasksIds) {
//                if (subTasks.get(subTaskId).getStatus().equals("IN_PROGRESS")) {
//                    epics.get(epicId).setStatus("IN_PROGRESS");
//                    break;
//                }
//            }
//            if (!epics.get(epicId).getStatus().equals("IN_PROGRESS")) {
//                for (int subTaskId : epics.get(epicId).subTasksIds) {
//                    if (subTasks.get(subTaskId).getStatus().equals("DONE")) {
//                        epics.get(epicId).setStatus("DONE");
//                    }
//                    break;
//                }
//            }
//            if (epics.get(epicId).getStatus().equals("DONE")) {
//                for (int subTaskId : epics.get(epicId).subTasksIds) {
//                    if (subTasks.get(subTaskId).getStatus().equals("NEW")) {
//                        epics.get(epicId).setStatus("IN_PROGRESS");
//                        break;
//                    }
//                }
//            } else {
//                epics.get(epicId).setStatus("NEW");
//            }
        } else {
            epic.setStatus("NEW");
        }
    }
    // Страшная консрукция, которая проверяет и меняет статус у эпика.
    // Можно было конечно попробовать enum, но наставник посоветовал отложить это до следующего спринта =)
}
