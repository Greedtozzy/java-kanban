package test;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.Managers;

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
        assertEquals(content[0], "id,type,name,status,description,epic");
        assertEquals(content[1], "1,TASK,1,NEW,1");
        assertEquals(content[3], "1");
    }

    /** Проверка развертки менеджера из файла. Условие - задача по id equals изначально созданной,
     * история содержит вызванную задачу.*/
    @Test
    public void shouldClassContainsAllTasksAfterLoadFromFile() {
        File file = new File("resources/recordedTasks.csv");
        Task task1 = new Task("1", "1");
        int id = manager.createNewTask(task1);
        int epicId = manager.createNewEpic(epic);
        manager.getTaskById(id);
        FileBackedTasksManager loadManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(loadManager.getTaskById(id), task1);
        assertEquals(loadManager.getEpicById(epicId), epic);
        assertTrue(loadManager.getHistory().contains(task1));
    }
}