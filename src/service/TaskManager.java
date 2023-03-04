package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void createNewTask(Task task);
    // Метод, создающий новую задачу

    void createNewEpic(Epic epic);
    // Метод, создающий новый эпик

    void createNewSubTask(SubTask subTask);
    // Метод, создающий новую подзадачу

    void deleteAllTasks();
    // Метод, удаляющий все задачи

    void deleteAllEpics();
    // Метод, удаляющий все эпики. Вместе с ними удаляет все подзадачи.

    void deleteAllSubTasks();
    // Метод, удаляющий все подзадачи. Так-же очищяет все списки подзадач во всех эпиках.

    void deleteTaskById(int taskId);
    // Метод, удаляющий задачу по требуемуму id

    void deleteEpicById(int epicId);
    /* Метод, удаляющий эпик по требуемуму id.
    Так-же удаляет все подзадачи, связавнные с эпиком
     */

    void deleteSubTaskById(int subTaskId);
    /* Метод, удаляющий подзадачу по требуемуму id.
    Так-же удаляет id подзадачи из коллекции в эпике, к которому пренадлежала задача
    */

    void updateTask(Task task);
    // Метод, обновляющий задачу. По сути просто заменяет в мапе одну задачу на другую.

    void updateEpic(Epic epic);
    /* Метод, обновляющий эпик. Принцип тот-же, что и с задачей.
     Но добавлен перенос в новый эпик списка подзадач из старого.
     */

    void updateSubTask(SubTask subTask);
    // Метод, обновляющий подзадачу.

    ArrayList getListAllTasks();
    // Метод, выводящий список всех задач.

    ArrayList getListAllEpics();
    // Метод, выводящий список всех эпиков.

    ArrayList getListAllSubTasks();
    // Метод, выводящий список всех подзадач.

    Task getTaskById(int id);
    /* Метод, выводящий задачу по id.
    Здесь может вернуть null, если по id передать не существующую задачу. Дальше то-же самое.
    Проверку можно провести при выводе в консоли (перед передачей в front).
    Наставник склазал, что здесь нужно инсключение бросать, но это мы будем проходить позже.
     */

    Epic getEpicById(int id);
    // Метод, выводящий эпик по id.

    SubTask getSubTaskById(int id);
    // Метод, выводящий подзадачу по id.

    ArrayList getSubTasksByEpicId(int id);
    // Метод, выводящий список подзадач по id эпика.

    void checkEpicStatus(int epicId);

    List<Task> getHistory();
}
