package service;

import exceptions.TaskCreatingException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    Task task;
    Epic epic;
    SubTask subTask;

    /** Проверка создания задачи. Условие - переданная задача equals созданной.*/
    @Test
    public void shouldTaskEqualsGetTaskAfterCreateTask() throws TaskCreatingException {
        int id = manager.createNewTask(task);
        assertEquals(manager.getTaskById(id), task);
    }

    /** Проверка создания задачи. Условие - при попытке созадния задачи из null, задача не создасться.*/
    @Test
    public void shouldBeNullIfCreateNullTask() {
        TaskCreatingException exception = assertThrows(TaskCreatingException.class ,
                () -> manager.createNewTask(null));
    }

    /** Проверка создания эпика. Условие - переданный эпик equals созданному.*/
    @Test
    public void shouldEpicEqualsGetEpicAfterCreateEpic() {
        int id = manager.createNewEpic(epic);
        assertEquals(manager.getEpicById(id), epic);
    }

    /** Проверка создания эпика. Условие - при попытке созадния эпика из null, эпик не создасться.
     * По запросу эпика по id (0) вернется null.*/
    @Test
    public void shouldBeNullIfCreateNullEpic() {
        int id = manager.createNewEpic(null);
        assertNull(manager.getEpicById(id));
    }

    /** Проверка создания подзадачи. Условие - переданная подзадача equals созданной.*/
    @Test
    public void shouldSubTaskEqualsGetSubTaskAfterCreateSubTask() throws TaskCreatingException {
        manager.createNewEpic(epic);
        int id = manager.createNewSubTask(subTask);
        assertEquals(manager.getSubTaskById(id), subTask);
    }

    /** Проверка создания подзадачи. Условие - при попытке созадния подхзадачи из null, подзадача не создасться.
     * По запросу подзадачи по id (0) вернется null.*/
    @Test
    public void shouldBeNullIfCreateNullSubTask() {
        manager.createNewEpic(epic);
        TaskCreatingException e = assertThrows(TaskCreatingException.class,
                () -> manager.createNewSubTask(null));
    }

    /** Проверка создания подзадачи. Условие - верное присвоение подзадаче номера эпика (1), переданного при создании.*/
    @Test
    public void shouldEpicIdEquals1AfterCreateSubTask() throws TaskCreatingException {
        manager.createNewEpic(epic);
        int id = manager.createNewSubTask(subTask);
        assertEquals(manager.getSubTaskById(id).getEpicId(), 1);
    }

    /** Проверка удаления всех задач. Условие - коллекция tasks.isEmpty().*/
    @Test
    public void shouldTasksBeEmptyAfterDeleteAllTasks() throws TaskCreatingException {
        manager.createNewTask(task);
        manager.deleteAllTasks();
        assertTrue(manager.getListAllTasks().isEmpty());
    }

    /** Проверка удаления всех эпиков. Условие - коллекции epics.isEmpty(), subTasks.isEmpty().*/
    @Test
    public void shouldEpicsAndSubTasksListsBeEmptyAfterDeleteAllEpics() throws TaskCreatingException {
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.deleteAllEpics();
        assertTrue(manager.getListAllEpics().isEmpty());
        assertTrue(manager.getListAllSubTasks().isEmpty());
    }

    /** Проверка удаления всех подзадач. Условие - коллекция subTasks.isEmpty,
     * коллекция id подзадач в эпике subTasksIds.isEmpty()*/
    @Test
    public void shouldSubTasksListAndSubTasksInEpicBeEmptyAfterDeleteAllSubTasks() throws TaskCreatingException {
        int id = manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.deleteAllSubTasks();
        assertTrue(manager.getListAllSubTasks().isEmpty());
        assertTrue(manager.getEpicById(id).subTasksIds.isEmpty());
    }

    /** Проверка удаления задачи по id. Условие - запрос удаленной здачи по id - null.*/
    @Test
    public void shouldGetTaskNullAfterDeleteTaskById() throws TaskCreatingException {
        int id = manager.createNewTask(task);
        manager.deleteTaskById(id);
        TreeSet<Task> empty = manager.getPrioritizedTasks();
        assertNull(manager.getTaskById(id));
        assertFalse(empty.contains(task));
    }

    /** Проверка удаления эпика по id. Условие - запрос удаленного эпика по id - null,
     * запрос коллекции подзадачь по id эпика возвращает пустую коллекцию.*/
    @Test
    public void shouldGetEpicByIdAndSubTasksByEpicsIdNullAfterDeleteEpicById() throws TaskCreatingException {
        int epicId = manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.deleteEpicById(epicId);
        assertNull(manager.getEpicById(epicId));
        assertTrue(manager.getSubTasksByEpicId(epicId).isEmpty());
    }

    /** Проверка удаления подзадачи по id. Условие - запрос удаленной подзадачи по id - null,
     * в спискке подзадач эпика не содержится id подзадачи.*/
    @Test
    public void shouldGetSubTaskByIdAndEpicsSubTaskListNullAfterDeleteSubTaskById() throws TaskCreatingException {
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        manager.deleteSubTaskById(subTaskId);
        assertNull(manager.getSubTaskById(subTaskId));
        assertFalse(manager.getEpicById(epicId).subTasksIds.contains(subTaskId));
    }

    /** Проверка обновления задачи. Условие - запрос обновленной задачи по id equals новой задаче*/
    @Test
    public void shouldGetTaskByIdEqualsNewTaskAfterUpdate() throws TaskCreatingException {
        int id = manager.createNewTask(task);
        Task updateTask = new Task("1", "1", TaskStatus.IN_PROGRESS, id,
                LocalDateTime.of(2023, 5, 30, 0, 0), 15);
        manager.updateTask(updateTask);
        assertEquals(manager.getTaskById(id), updateTask);
    }

    /** Проверка обновления эпика. Условие - запрос обновленного эпика по id equals новому эпику.
     * Возникли сложности с сравнением. Пришлось построчно пробегать по полям.*/
    @Test
    public void shouldGetEpicByIdEqualsNewEpicAfterUpdate() throws TaskCreatingException {
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        Epic newEpic = new Epic("2", "2", epicId);
        manager.updateEpic(newEpic);
        Epic updatedEpic = manager.getEpicById(epicId);
        assertEquals(updatedEpic.getName(), newEpic.getName());
        assertEquals(updatedEpic.getDescription(), newEpic.getDescription());
        assertTrue(updatedEpic.subTasksIds.contains(subTaskId));
    }

    /** Проверка обновления подзадачи. Условие - запрос обновленной подзадачи по id equals новой подзадаче*/
    @Test
    public void shouldGetSubTaskByIdEqualsNewSubTaskAfterUpdate() throws TaskCreatingException {
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        SubTask newSubTask = new SubTask("2", "2", TaskStatus.IN_PROGRESS, subTaskId, epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15);
        manager.updateSubTask(newSubTask);
        assertEquals(manager.getSubTaskById(subTaskId), newSubTask);
    }

    /** Проверка вывода списка всех задач. Условие - коллекция пустая до создания задач,
     * коллекция содержит созданные задачи.*/
    @Test
    public void shouldArrayContainsAllTasks() throws TaskCreatingException {
        ArrayList<Task> emptyList = manager.getListAllTasks();
        int id1 = manager.createNewTask(task);
        int id2 = manager.createNewTask(new Task("1", "1",
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        ArrayList<Task> allTasks = manager.getListAllTasks();
        assertTrue(emptyList.isEmpty());
        assertTrue(allTasks.contains(manager.getTaskById(id1)));
        assertTrue(allTasks.contains(manager.getTaskById(id2)));
    }

    /** Проверка вывода списка всех эпиков. Условие - коллекция пустая до создания эпиков,
     * коллекция содержит созданные эпики.*/
    @Test
    public void shouldArrayContainsAllEpics() {
        ArrayList<Task> emptyList = manager.getListAllEpics();
        int id1 = manager.createNewEpic(epic);
        int id2 = manager.createNewEpic(new Epic("1", "1"));
        ArrayList<Task> allEpics = manager.getListAllEpics();
        assertTrue(emptyList.isEmpty());
        assertTrue(allEpics.contains(manager.getEpicById(id1)));
        assertTrue(allEpics.contains(manager.getEpicById(id2)));
    }

    /** Проверка вывода списка всех подзадач. Условие - коллекция пустая до создания подзадач,
     * коллекция содержит созданные подзадачи.*/
    @Test
    public void shouldArrayContainsAllSubTasks() throws TaskCreatingException {
        ArrayList<Task> emptyList = manager.getListAllSubTasks();
        int epicId = manager.createNewEpic(epic);
        int subTaskId1 = manager.createNewSubTask(subTask);
        int subTaskId2 = manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 6, 29, 0, 0), 15));
        ArrayList<Task> allEpics = manager.getListAllSubTasks();
        assertTrue(emptyList.isEmpty());
        assertTrue(allEpics.contains(manager.getSubTaskById(subTaskId1)));
        assertTrue(allEpics.contains(manager.getSubTaskById(subTaskId2)));
    }

    /** Проверка вызова задачи по id. Условие - вызванная задача equals созданной, null при вызове по не верному id.*/
    @Test
    public void shouldGetTaskByIdEqualsTaskAndNullUncreatedTask() throws TaskCreatingException {
        int id = manager.createNewTask(task);
        assertEquals(manager.getTaskById(id), task);
        assertNull(manager.getTaskById(2));
    }

    /** Проверка вызова эпика по id. Условие - вызванный эпик equals созданному, null при вызове по не верному id.*/
    @Test
    public void shouldGetEpicByIdEqualsEpicAndNullUncreatedEpic() {
        int id = manager.createNewEpic(epic);
        assertEquals(manager.getEpicById(id), epic);
        assertNull(manager.getEpicById(2));
    }

    /** Проверка вызова подзадачи по id. Условие - вызванная подзадача equals созданной, null при вызове по не верному id.*/
    @Test
    public void shouldGetSubTaskByIdEqualsSubTaskAndNullUncreatedSubTask() throws TaskCreatingException {
        manager.createNewEpic(epic);
        int id = manager.createNewSubTask(subTask);
        assertEquals(manager.getSubTaskById(id), subTask);
        assertNull(manager.getSubTaskById(3));
    }

    /** Проверка вызова коллекции всех подзадач по их epicId.
     * Условие - коллекция contains все созданные подзадачи с одним epicId, коллекция пустая, если нет подзадач.*/
    @Test
    public void shouldArrayContainsAllSubTasksByEpicId() throws TaskCreatingException {
        int epicId = manager.createNewEpic(epic);
        ArrayList<Task> emptyArray = manager.getSubTasksByEpicId(epicId);
        int subTaskId1 = manager.createNewSubTask(subTask);
        int subTaskId2 = manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 6, 29, 0, 0), 15));
        ArrayList<Task> subTasksByEpicId = manager.getSubTasksByEpicId(epicId);
        assertTrue(emptyArray.isEmpty());
        assertTrue(subTasksByEpicId.contains(manager.getSubTaskById(subTaskId1)));
        assertTrue(subTasksByEpicId.contains(manager.getSubTaskById(subTaskId2)));
    }

    /** Проверка вызова истории. Условие - история пустая, если не было вызовов задач.*/
    @Test
    public void shouldGetHistoryIsEmptyNoGetTasks() throws TaskCreatingException {
        manager.createNewTask(task);
        List<Task> emptyList = manager.getHistory();
        assertTrue(emptyList.isEmpty());
    }

    /** Проверка вызова истории. Условие - история содержит все вызовы задач.*/
    @Test
    public void shouldGetHistoryContainsAllGetTasks() throws TaskCreatingException {
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        int taskId = manager.createNewTask(task);
        manager.getTaskById(taskId);
        manager.getEpicById(epicId);
        manager.getSubTaskById(subTaskId);
        assertTrue(manager.getHistory().contains(task));
        assertTrue(manager.getHistory().contains(subTask));
        assertTrue(manager.getHistory().contains(epic));
    }

    /** Проверка вызова истории. Условие - повторный вызов задачи не дублирует её в истории.*/
    @Test
    public void shouldNoDoublesInArrayIfGetTaskTwice() throws TaskCreatingException {
        int id = manager.createNewTask(task);
        manager.getTaskById(id);
        manager.getTaskById(id);
        assertEquals(manager.getHistory().size(), 1);
    }

    /** Проверка присваивания времени в создаваемой задаче. Условие - время в созданной задаче equals времени в передаваемой*/
    @Test
    public void shouldTaskStartTimeEqualsStartTime() throws TaskCreatingException {
        int id = manager.createNewTask(task);
        assertEquals(manager.getTaskById(id).getStartTime(), task.getStartTime());
        assertEquals(manager.getTaskById(id).getEndTime(), task.getEndTime());
    }

    /** Проверка пересечений времени выполнения 2-х задач. Условие - При пересечении задача не создается, id = 0.*/
    @Test
    public void shouldNoCreatingTaskIfTimeBook() throws TaskCreatingException {
        manager.createNewTask(task);
        TaskCreatingException exception = assertThrows(TaskCreatingException.class,
                () -> manager.createNewTask(new Task("1", "1",
                LocalDateTime.of(2023, 5, 30, 0, 8), 15)));
    }

    /** Проверка сортировки задач. Условие - верная задача должна быть первой в TreeSet*/
    @Test
    public void shouldCorrectTaskBeFirstInGetPrioritizedTasks() throws TaskCreatingException {
        manager.createNewTask(task);
        manager.createNewTask(new Task("1", "1",
                LocalDateTime.of(2023, 6, 30, 0, 0), 15));
        TreeSet<Task> getPrioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(getPrioritizedTasks.first(), task);
    }
}