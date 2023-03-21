package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1; // id задачи начинает отсчет от 1
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    HashMap<Integer, Task> tasks = new HashMap<>(); // Хранение задач
    HashMap<Integer, Epic> epics = new HashMap<>(); // Хранение эпиков
    HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Хранение подзадач

    @Override
    public int createNewTask(Task task) {
        tasks.put(id, task);
        task.setId(id);
        return id++;
    }
    // Метод, создающий новую задачу

    @Override
    public int createNewEpic(Epic epic) {
        epics.put(id, epic);
        epic.setId(id);
        return id++;
    }
    // Метод, создающий новый эпик

    @Override
    public int createNewSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTasks.put(id, subTask);
            epics.get(subTask.getEpicId()).addSubTaskId(id);
            subTask.setId(id);
            checkEpicStatus(subTask.getEpicId());
            return id++;
        } else {
            System.out.println("Вы пытаетесь добавить подзадачу к не существующему эпику. Проверьте epicId.");
            return 0;
        }
    }
    // Метод, создающий новую подзадачу

    @Override
    public void deleteAllTasks() {
        tasks.clear();

    }
    // Метод, удаляющий все задачи

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }
    // Метод, удаляющий все эпики. Вместе с ними удаляет все подзадачи.

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.subTasksIds.clear();
            checkEpicStatus(epic.getId());
        }
        subTasks.clear();
    }
    // Метод, удаляющий все подзадачи. Так-же очищяет все списки подзадач во всех эпиках.

    @Override
    public void deleteTaskById(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }
    // Метод, удаляющий задачу по требуемуму id

    @Override
    public void deleteEpicById(int epicId) {
        historyManager.remove(epicId);
        Epic epic = epics.remove(epicId);
        if(epic !=null) {
            for (Integer subtaskId : epic.getSubTasksIds()) {
                historyManager.remove(subtaskId);
                subTasks.remove(subtaskId);
            }
        }
    }
    /* Метод, удаляющий эпик по требуемуму id.
    Так-же удаляет все подзадачи, связавнные с эпиком
     */

    @Override
    public void deleteSubTaskById(int subTaskId) {
        historyManager.remove(subTaskId);
        SubTask subTask = subTasks.remove(subTaskId);
        if (subTask != null) {
            int epicId = subTask.getEpicId();
            epics.get(epicId).subTasksIds.remove((Integer) subTaskId);
            checkEpicStatus(epicId);
        }
    }
    /* Метод, удаляющий подзадачу по требуемуму id.
    Так-же удаляет id подзадачи из коллекции в эпике, к которому пренадлежала задача
    */

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Нет задачи с таким id.");
        }
    }
    // Метод, обновляющий задачу. По сути просто заменяет в мапе одну задачу на другую.

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            final Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        } else {
            System.out.println("Нет эпика с таким id");
        }
    }
    /* Метод, обновляющий эпик. Принцип тот-же, что и с задачей.
     Но добавлен перенос в новый эпик списка подзадач из старого.
     */

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            int epicId = subTasks.get(subTask.getId()).getEpicId();
            if (epics.containsKey(subTask.getEpicId())) {
                subTasks.put(subTask.getId(), subTask);
                checkEpicStatus(epicId);
                if (epicId != subTask.getEpicId()) {
                    checkEpicStatus(subTask.getEpicId());
                }
            } else {
                System.out.println("Нет эпика, к которому привязана эта подзадача");
            }
        } else {
            System.out.println("Нет подзадачи с таким id");
        }
    }
    // Метод, обновляющий подзадачу.

    @Override
    public ArrayList getListAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }
    // Метод, выводящий список всех задач.

    @Override
    public ArrayList getListAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }
        return allEpics;
    }
    // Метод, выводящий список всех эпиков.

    @Override
    public ArrayList getListAllSubTasks() {
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTasks.get(id));
        }
        return allSubTasks;
    }
    // Метод, выводящий список всех подзадач.

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addTaskInHistory(task);
        return task;
    }
    // Метод, выводящий задачу по id.

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addTaskInHistory(epic);
        return epic;
    }
    // Метод, выводящий эпик по id.

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.addTaskInHistory(subTask);
        return subTask;
    }
    // Метод, выводящий подзадачу по id.

    @Override
    public ArrayList getSubTasksByEpicId(int id) {
        ArrayList<SubTask> subTasksByEpic = new ArrayList<>();
        for (int subTaskId : epics.get(id).subTasksIds) {
            subTasksByEpic.add(subTasks.get(subTaskId));
        }
        return subTasksByEpic;
    }
    // Метод, выводящий список подзадач по id эпика.

    @Override
    public void checkEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epic.subTasksIds.isEmpty()) {
            TaskStatus status = null;
            for (int id : epic.getSubTasksIds()) {
                final SubTask subtask = subTasks.get(id);
                if (status == null) {
                    status = subtask.getStatus();
                    continue;
                }
                if (status.equals(subtask.getStatus())
                        && !status.equals(TaskStatus.IN_PROGRESS)) {
                    continue;
                }
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
            epic.setStatus(status);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }
    // Метод, который проверяет и меняет статус у эпика.
    @Override
    public List<Task> getHistory() {
       return historyManager.getHistory();
    }
}
