package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic;
    TaskManager manager;
    int epicId;

    @BeforeEach
    public void createEpic() {
        manager = Managers.getDefault();
        epic = new Epic("1", "1");
        epicId = manager.createNewEpic(epic);
    }

    @Test
    public void shouldSetNewStatusToEpicNoSubTasks() {
        TaskStatus status = manager.getEpicById(epicId).getStatus();
        assertEquals(TaskStatus.NEW, status);
    }

    @Test
    public void shouldSetNewStatusToEpicWithNewStatusInSubTask() {
        manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        TaskStatus status = manager.getEpicById(epicId).getStatus();
        assertEquals(TaskStatus.NEW, status);
    }

    @Test
    public void shouldSetInProgressStatusWithNewDoneInSubTasks() {
        int subTaskId = manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 6, 29, 0, 0), 15));
        manager.updateSubTask(new SubTask("1", "1", TaskStatus.DONE, subTaskId, epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        TaskStatus status = manager.getEpicById(epicId).getStatus();
        assertEquals(TaskStatus.IN_PROGRESS, status);
    }

    @Test
    public void shouldSetDoneStatusWithDoneInSubTask() {
        int subTaskId = manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        manager.updateSubTask(new SubTask("1", "1", TaskStatus.DONE, subTaskId, epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        TaskStatus status = manager.getEpicById(epicId).getStatus();
        assertEquals(TaskStatus.DONE, status);
    }

    @Test
    public void shouldSetInProgressStatusWithInProgressInSubTask() {
        int subTaskId = manager.createNewSubTask(new SubTask("1", "1", epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        manager.updateSubTask(new SubTask("1", "1", TaskStatus.IN_PROGRESS, subTaskId, epicId,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15));
        TaskStatus status = manager.getEpicById(epicId).getStatus();
        assertEquals(TaskStatus.IN_PROGRESS, status);
    }
}