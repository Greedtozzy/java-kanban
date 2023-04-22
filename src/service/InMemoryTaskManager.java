package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1; // id задачи начинает отсчет от 1
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    /** Хранение задач*/
    HashMap<Integer, Task> tasks = new HashMap<>();
    /** Хранение эпиков*/
    HashMap<Integer, Epic> epics = new HashMap<>();
    /** Хранение подзадач*/
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    /** Хранение временных помежутков и информации о размещении в них задач*/
    HashMap<LocalDateTime, Boolean> timeIntervals = timeGrid();
    /** Хранение отсортированных задач и подзадач.*/
    TreeSet<Task> sortedTasks = new TreeSet<>();

    /** Метод, создающий новую задачу*/
    @Override
    public int createNewTask(Task task) {
        if (task != null) {
            if (checkTimeInterval(task)) {
                tasks.put(id, task);
                task.setId(id);
                return id++;
            } else {
                System.out.println("Временной интервал занят");
                return 0;
            }
        } else {
            System.out.println("Задача == null");
            return 0;
        }
    }

    /** Метод, создающий новый эпик*/
    @Override
    public int createNewEpic(Epic epic) {
        if (epic != null) {
            epics.put(id, epic);
            epic.setId(id);
            return id++;
        } else {
            System.out.println("Эпик == null");
            return 0;
        }
    }

    /** Метод, создающий новую подзадачу*/
    @Override
    public int createNewSubTask(SubTask subTask) {
        if (subTask != null) {
            if (checkTimeInterval(subTask)) {
                if (epics.containsKey(subTask.getEpicId())) {
                    subTasks.put(id, subTask);
                    epics.get(subTask.getEpicId()).addSubTaskId(id);
                    subTask.setId(id);
                    checkEpicStatus(subTask.getEpicId());
                    setTimeToEpic(epics.get(subTask.getEpicId()));
                    return id++;
                } else {
                    System.out.println("Вы пытаетесь добавить подзадачу к не существующему эпику. Проверьте epicId.");
                    return 0;
                }
            } else {
                System.out.println("Временной интервал занят");
                return 0;
            }
        } else {
            System.out.println("Подзадача == null");
            return 0;
        }
    }

    /** Метод, удаляющий все задачи*/
    @Override
    public void deleteAllTasks() {
        tasks.clear();

    }

    /** Метод, удаляющий все эпики. Вместе с ними удаляет все подзадачи.*/
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    /** Метод, удаляющий все подзадачи. Так-же очищяет все списки подзадач во всех эпиках.*/
    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.subTasksIds.clear();
            checkEpicStatus(epic.getId());
            setTimeToEpic(epic);
        }
    }

    /** Метод, удаляющий задачу по требуемуму id*/
    @Override
    public void deleteTaskById(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    /** Метод, удаляющий эпик по требуемуму id.
     Так-же удаляет все подзадачи, связавнные с эпиком.*/
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

    /** Метод, удаляющий подзадачу по требуемуму id.
     Так-же удаляет id подзадачи из коллекции в эпике, к которому пренадлежала задача.*/
    @Override
    public void deleteSubTaskById(int subTaskId) {
        historyManager.remove(subTaskId);
        SubTask subTask = subTasks.remove(subTaskId);
        if (subTask != null) {
            int epicId = subTask.getEpicId();
            epics.get(epicId).subTasksIds.remove((Integer) subTaskId);
            checkEpicStatus(epicId);
            setTimeToEpic(epics.get(epicId));
        }
    }

    /** Метод, обновляющий задачу. По сути просто заменяет в мапе одну задачу на другую.*/
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            if ((task.getStartTime().equals(oldTask.getStartTime()) &&
                    task.getDurationInMinutes() == oldTask.getDurationInMinutes()) ||
                    checkTimeInterval(task)) {
                tasks.put(task.getId(), task);
            } else {
                System.out.println("Временной интервал занят");
            }
        } else {
            System.out.println("Нет задачи с таким id.");
        }
    }

    /** Метод, обновляющий эпик.*/
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

    /** Метод, обновляющий подзадачу.*/
    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            SubTask oldSubTask = subTasks.get(subTask.getId());
            if ((subTask.getStartTime().equals(oldSubTask.getStartTime()) &&
                    subTask.getDurationInMinutes() == oldSubTask.getDurationInMinutes()) || checkTimeInterval(subTask)) {
                int epicId = subTasks.get(subTask.getId()).getEpicId();
                if (epics.containsKey(subTask.getEpicId())) {
                    subTasks.put(subTask.getId(), subTask);
                    checkEpicStatus(epicId);
                    setTimeToEpic(epics.get(epicId));
                    if (epicId != subTask.getEpicId()) {
                        checkEpicStatus(subTask.getEpicId());
                        setTimeToEpic(epics.get(subTask.getEpicId()));
                    }
                } else {
                    System.out.println("Нет эпика, к которому привязана эта подзадача");
                }
            } else {
                System.out.println("Временной интервал занят");
            }
        } else {
            System.out.println("Нет подзадачи с таким id");
        }
    }

    /** Метод, выводящий список всех задач.*/
    @Override
    public ArrayList getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    /** Метод, выводящий список всех эпиков.*/
    @Override
    public ArrayList getListAllEpics() {
        return new ArrayList<>(epics.values());
    }

    /** Метод, выводящий список всех подзадач.*/
    @Override
    public ArrayList getListAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    /** Метод, выводящий задачу по id.*/
    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) != null) {
            Task task = tasks.get(id);
            historyManager.addTaskInHistory(task);
            return task;
        } else {
            System.out.println("Нет задачи с таким id");
            return null;
        }
    }

    /** Метод, выводящий эпик по id.*/
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addTaskInHistory(epic);
        return epic;
    }

    /** Метод, выводящий подзадачу по id.*/
    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.addTaskInHistory(subTask);
        return subTask;
    }

    /** Метод, выводящий список подзадач по id эпика.*/
    @Override
    public ArrayList getSubTasksByEpicId(int id) {

        ArrayList<SubTask> subTasksByEpic = new ArrayList<>();
        if (epics.get(id) != null) {
            for (int subTaskId : epics.get(id).subTasksIds) {
                subTasksByEpic.add(subTasks.get(subTaskId));
            }
        } else {
            System.out.println("Нет эпика с таким id.");
        }
        return subTasksByEpic;
    }

    /** Метод, который проверяет и меняет статус у эпика.*/
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

    @Override
    public List<Task> getHistory() {
       return historyManager.getHistory();
    }

    protected Task findTask(Integer id) {
        final Task task = tasks.get(id);
        if (task != null) {
            return task;
        }
        final SubTask subtask = subTasks.get(id);
        if (subtask != null) {
            return subtask;
        }
        return epics.get(id);
    }

    /** Метод, устанавливающий в епик startTime, endTime, duration*/
    @Override
    public void setTimeToEpic(Epic epic) {
        long duration = 0;
        int checker = 0;
        if (!epic.subTasksIds.isEmpty()) {
            LocalDateTime startTime = LocalDateTime.of(99999, 1, 1, 0, 0);
            LocalDateTime endTime = LocalDateTime.of(0, 1, 1, 0, 0);
            for (int subTaskId : epic.subTasksIds) {
                if (subTasks.get(subTaskId).getStartTime() != null) {
                    checker++;
                    if (subTasks.get(subTaskId).getStartTime().isBefore(startTime))
                        startTime = subTasks.get(subTaskId).getStartTime();
                    if (subTasks.get(subTaskId).getEndTime().isAfter(endTime))
                        endTime = subTasks.get(subTaskId).getEndTime();
                    duration += subTasks.get(subTaskId).getDurationInMinutes();
                }
            }
            if (checker != 0) {
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
                epic.setDurationInMinutes(duration);
            }
        }
    }

    /** Метод, возвращающий мапу с временными интервалами*/
    @Override
    public HashMap<LocalDateTime, Boolean> timeGrid() {
        HashMap<LocalDateTime, Boolean> timeGrid = new HashMap<>();
        LocalDateTime interval = LocalDateTime.of(2023, 5, 1, 0, 0);
        while(!interval.equals(LocalDateTime.of(2024, 5, 1, 0, 0))) {
            timeGrid.put(interval, true);
            interval = interval.plusMinutes(15);
        }
        return timeGrid;
    }

    /** Метод, проверяющий пересечения переданной в него задачи с другими*/
    @Override
    public boolean checkTimeInterval(Task task) {
        boolean check = true;
        if (!(task.getStartTime() == null)) {
            LocalDateTime start = task.getStartTime();
            LocalDateTime roundingStart = LocalDateTime.of(start.getYear(),
                    start.getMonth(), start.getDayOfMonth(), start.getHour(),
                    start.getMinute() - (start.getMinute() % 15));
            for (int i = 0; i < (task.getDurationInMinutes() / 15); i++) {
                if (!timeIntervals.get(roundingStart.plusMinutes(15L * i)).equals(true)) {
                    check = false;
                }
                timeIntervals.put(roundingStart.plusMinutes(15L * i), false);
            }
        }
        return check;
    }

    /** Метод, заполняющий список с отсортированными задачами и подзадачами.*/
    @Override
    public TreeSet<Task> getSortedTasks() {
        TreeSet<Task> sorted = new TreeSet<>();
        sorted.addAll(tasks.values());
        sorted.addAll(subTasks.values());
        return sorted;
    }

    /** Метод, возвращающий отсортированный список задач и подзадач.*/
    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        sortedTasks = getSortedTasks();
        return sortedTasks;
    }
}
