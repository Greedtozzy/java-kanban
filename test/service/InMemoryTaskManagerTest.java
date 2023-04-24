package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import service.Managers;

import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest{
    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        task = new Task("1", "1",
                LocalDateTime.of(2023, 5, 30, 0, 0), 15);
        epic = new Epic("1", "1");
        subTask = new SubTask("1", "1", 1,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15);
    }
}