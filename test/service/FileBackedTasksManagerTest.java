package service;

import exceptions.TaskCreatingException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {
    @BeforeEach
    public void beforeEach() {
        manager = Managers.getFileBackedTaskManager();
        task = new Task("1", "1",
                LocalDateTime.of(2023, 5, 30, 0, 0), 15);
        epic = new Epic("1", "1");
        subTask = new SubTask("1", "1", 1,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15);
    }

    /** Проверка записи в файл. Условие - 1, 2 строки из файла соответствуют заданным.*/
    @Test
    public void shouldSaveTextToFileAfterCreateTask() throws IOException {
        File file = new File("resources/recordedTasks.csv");
        int id = manager.createNewTask(task);
        manager.getTaskById(id);
        String[] content = Files.readString(file.toPath()).split(System.lineSeparator());
        assertEquals(content[0], "id,type,name,status,description,startTime,duration,epic");
        assertEquals(content[1], "1,TASK,1,NEW,1,2023-05-30T00:00,15");
        assertEquals(content[3], "1");
    }

    /** Проверка развертки менеджера из файла. Условие - задача по id equals изначально созданной,
     * история содержит вызванную задачу.*/
    @Test
    public void shouldClassContainsAllTasksAfterLoadFromFile() {
        File file = new File("resources/recordedTasks.csv");
        int epicId = manager.createNewEpic(epic);
        int id = manager.createNewTask(task);
        int subTaskId = manager.createNewSubTask(subTask);
        int subTaskId2 = manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 5, 29, 1, 30), 15));
        manager.updateSubTask(new SubTask("1", "1", TaskStatus.DONE, subTaskId, epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        manager.getTaskById(id);
        FileBackedTasksManager loadManager = FileBackedTasksManager.loadFromFile(file);
        System.out.println(manager.getEpicById(epicId));
        System.out.println(loadManager.getEpicById(epicId));
        assertEquals(loadManager.getTaskById(id), manager.getTaskById(id));
        assertEquals(loadManager.getEpicById(epicId), manager.getEpicById(epicId));
        assertEquals(loadManager.getSubTaskById(subTaskId), manager.getSubTaskById(subTaskId));
        assertTrue(loadManager.getHistory().contains(task));

    }
}