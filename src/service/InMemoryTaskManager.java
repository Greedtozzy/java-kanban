package service;

import exceptions.TaskCreatingException;
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
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    /** Хранение эпиков*/
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    /** Хранение подзадач*/
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    /** Хранение отсортированных задач и подзадач.*/
    protected final TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    /** Метод, создающий новую задачу*/
    @Override
    public int createNewTask(Task task) throws TaskCreatingException {
        if (task != null) {
            if (checkTimeInterval(task)) {
                tasks.put(id, task);
                sortedTasks.add(task);
                task.setId(id);
                return id++;
            } else {
                throw new TaskCreatingException("Временной интервал занят");
            }
        } else {
            throw new TaskCreatingException("Задача == null");
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
    public int createNewSubTask(SubTask subTask) throws TaskCreatingException {
        if (subTask == null) {
            throw new TaskCreatingException("Подзадача == null");
        }
        if (!checkTimeInterval(subTask)) {
            throw new TaskCreatingException("Временной интервал занят");
        }
        if (!epics.containsKey(subTask.getEpicId())) {
            throw new TaskCreatingException("Не верный epicId");
        }
        subTasks.put(id, subTask);
        sortedTasks.add(subTask);
        epics.get(subTask.getEpicId()).addSubTaskId(id);
        subTask.setId(id);
        checkEpicStatus(subTask.getEpicId());
        setTimeToEpic(epics.get(subTask.getEpicId()));
        return id++;
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
        sortedTasks.remove(tasks.remove(taskId));
    }

    /** Метод, удаляющий эпик по требуемуму id.
     Так-же удаляет все подзадачи, связавнные с эпиком.*/
    @Override
    public void deleteEpicById(int epicId) {
        historyManager.remove(epicId);
        Epic epic = epics.remove(epicId);
        if (epic !=null) {
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
        sortedTasks.remove(subTask);
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
                sortedTasks.remove(oldTask);
                tasks.put(task.getId(), task);
                sortedTasks.add(task);
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
                    sortedTasks.remove(oldSubTask);
                    subTasks.put(subTask.getId(), subTask);
                    sortedTasks.add(subTask);
                    checkEpicStatus(epicId);
                    setTimeToEpic(epics.get(epicId));
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
    protected void setTimeToEpic(Epic epic) {
        long duration = 0;
        if (!epic.subTasksIds.isEmpty()) {
            LocalDateTime startTime = LocalDateTime.of(99999, 1, 1, 0, 0);
            LocalDateTime endTime = LocalDateTime.of(0, 1, 1, 0, 0);
            for (int subTaskId : epic.subTasksIds) {
                if (subTasks.get(subTaskId).getStartTime() != null) {
                    if (subTasks.get(subTaskId).getStartTime().isBefore(startTime))
                        startTime = subTasks.get(subTaskId).getStartTime();
                    if (subTasks.get(subTaskId).getEndTime().isAfter(endTime))
                        endTime = subTasks.get(subTaskId).getEndTime();
                    duration += subTasks.get(subTaskId).getDurationInMinutes();
                }
            }
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
                epic.setDurationInMinutes(duration);
        }
    }

    /** Метод, проверяющий пересечения переданной в него задачи с другими*/
    @Override
    public boolean checkTimeInterval(Task task) {
        boolean check = true;
        for (Task sortedTask : sortedTasks) {
            if (!task.getEndTime().isBefore(sortedTask.getStartTime()) ||
                    task.getEndTime().equals(sortedTask.getStartTime())) {
                if (!task.getStartTime().isAfter(sortedTask.getEndTime()) ||
                        task.getStartTime().equals(sortedTask.getEndTime())) {
                    check = false;
                }
            }
        }
        return check;
    }

    /** Метод, возвращающий отсортированный список задач и подзадач.*/
    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }
}
