package service;

import client.KVTaskClient;
import com.google.gson.Gson;
import exceptions.ClientException;
import model.*;
import org.junit.jupiter.api.*;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {
    KVServer kvServer;
    TaskManager manager;
    HttpTaskServer taskServer;
    Task task;
    Epic epic;
    SubTask subTask;
    String uri_format;
    KVTaskClient kvClient;
    HttpClient client;
    Gson gson;

    @BeforeEach
    public void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getHttpTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
        kvClient = new KVTaskClient();
        client = HttpClient.newHttpClient();
        uri_format = "http://localhost:8080";
        gson = Managers.getGson();
        task = new Task("1", "1",
                LocalDateTime.of(2023, 5, 30, 0, 0), 15);
        epic = new Epic("1", "1");
        subTask = new SubTask("1", "1", 1,
                LocalDateTime.of(2023, 5, 29, 0, 0), 15);
    }

    @AfterEach
    public void stopServers() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    public void createNewTaskTest() {
        URI uri = URI.create(uri_format + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task), UTF_8))
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            task.setId(1);
            assertEquals(task, manager.getTaskById(1));
            assertEquals("Задача успешно создана. Id = 1", response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void createNewEpicTest() {
        URI uri = URI.create(uri_format + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic), UTF_8))
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            epic.setId(1);
            assertEquals(epic, manager.getEpicById(1));
            assertEquals("Эпик успешно создан. Id = 1", response.body());

        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void createNewSubTaskTest() {
        manager.createNewEpic(epic);
        URI uri = URI.create(uri_format + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask), UTF_8))
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            subTask.setId(2);
            assertEquals(subTask, manager.getSubTaskById(2));
            assertEquals("Подзадача успешно создана. Id = 2", response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void updateTaskTest() {
        manager.createNewTask(task);
        task.setId(1);
        URI uri = URI.create(uri_format + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task), UTF_8))
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(task, manager.getTaskById(1));
            assertEquals("Задача успешно обновлена. Id = 1", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void updateEpicTest() {
        manager.createNewEpic(epic);
        epic.setId(1);
        URI uri = URI.create(uri_format + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic), UTF_8))
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(epic, manager.getEpicById(1));
            assertEquals("Эпик успешно обновлен. Id = 1", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void updateSubTaskTest() {
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        epic.setId(1);
        subTask.setId(2);
        URI uri = URI.create(uri_format + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask), UTF_8))
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(subTask, manager.getSubTaskById(2));
            assertEquals("Подзадача успешно обновлена. Id = 2", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void deleteAllTasksTest() {
        int taskId = manager.createNewTask(task);
        URI uri = URI.create(uri_format + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertNull(manager.getTaskById(taskId));
            assertTrue(manager.getListAllTasks().isEmpty());
            assertEquals("Все задачи удалены", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void deleteAllEpicsTest() {
        int epicId = manager.createNewEpic(epic);
        URI uri = URI.create(uri_format + "/tasks/epic");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertNull(manager.getEpicById(epicId));
            assertTrue(manager.getListAllEpics().isEmpty());
            assertEquals("Все эпики удалены", response.body());
            assertEquals(200, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void deleteAllSubTasksTest() {
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        URI uri = URI.create(uri_format + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getEpicById(epicId).subTasksIds.isEmpty());
            assertNull(manager.getSubTaskById(subTaskId));
            assertTrue(manager.getListAllSubTasks().isEmpty());
            assertEquals("Все подзадачи удалены", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void deleteTaskByIdTest() {
        manager.createNewTask(task);
        task.setId(1);
        Task taskToDelete = new Task("2", "2",
                LocalDateTime.of(2023, 5, 29, 0, 0), 15);
        int taskId2 = manager.createNewTask(taskToDelete);
        URI uri = URI.create(uri_format + "/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertNull(manager.getTaskById(taskId2));
            assertTrue(manager.getListAllTasks().contains(task));
            assertFalse(manager.getListAllTasks().contains(taskToDelete));
            assertEquals("Задача с id = " + taskId2 + " удалена", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void deleteEpicByIdTest() {
        manager.createNewEpic(epic);
        epic.setId(1);
        Epic epicToDelete = new Epic("2", "2");
        int epicId2 = manager.createNewEpic(epicToDelete);
        URI uri = URI.create(uri_format + "/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertNull(manager.getEpicById(epicId2));
            assertTrue(manager.getListAllEpics().contains(epic));
            assertFalse(manager.getListAllEpics().contains(epicToDelete));
            assertEquals("Эпик с id = " + epicId2 + " удалена", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void deleteSubTaskByIdTest() {
        int epicId = manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        SubTask subTaskToDelete = new SubTask("1", "1", 1,
                LocalDateTime.of(2023, 5, 28, 0, 0), 15);
        int subTaskId2 = manager.createNewSubTask(subTaskToDelete);
        URI uri = URI.create(uri_format + "/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertNull(manager.getSubTaskById(subTaskId2));
            assertTrue(manager.getListAllSubTasks().contains(subTask));
            assertFalse(manager.getListAllSubTasks().contains(subTaskToDelete));
            assertTrue(manager.getSubTasksByEpicId(epicId).contains(subTask));
            assertFalse(manager.getSubTasksByEpicId(epicId).contains(subTaskToDelete));
            assertEquals("Подзадача с id = " + subTaskId2 + " удалена", response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getPrioritizedTasksTest() {
        Task task1 = new Task("2", "2",
                LocalDateTime.of(2023, 6, 30, 0, 0), 15);
        Task task2 = new Task("3", "3",
                LocalDateTime.of(2023, 4, 30, 0, 0), 15);
        manager.createNewTask(task);
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        URI uri = URI.create(uri_format + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getListAllTasksTest() {
        Task task1 = new Task("2", "2",
                LocalDateTime.of(2023, 6, 30, 0, 0), 15);
        Task task2 = new Task("3", "3",
                LocalDateTime.of(2023, 4, 30, 0, 0), 15);
        manager.createNewTask(task);
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        URI uri = URI.create(uri_format + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getListAllTasks()), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getListAllEpicsTest() {
        Epic epic1 = new Epic("2", "2");
        Epic epic2 = new Epic("3", "3");
        manager.createNewEpic(epic);
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);
        URI uri = URI.create(uri_format + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getListAllEpics()), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getListAllSubTasksTest() {
        SubTask subTask1 = new SubTask("2", "2", 1,
                LocalDateTime.of(2023, 5, 28, 0, 0), 15);
        SubTask subTask2 = new SubTask("3", "3", 1,
                LocalDateTime.of(2023, 5, 27, 0, 0), 15);
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        URI uri = URI.create(uri_format + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getListAllSubTasks()), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getListAllSubTasksByEpicIdTest() {
        SubTask subTask1 = new SubTask("2", "2", 1,
                LocalDateTime.of(2023, 5, 28, 0, 0), 15);
        SubTask subTask2 = new SubTask("3", "3", 1,
                LocalDateTime.of(2023, 5, 27, 0, 0), 15);
        int epicId = manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        URI uri = URI.create(uri_format + "/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getSubTasksByEpicId(epicId)), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getTaskByIdTest() {
        int taskId = manager.createNewTask(task);
        URI uri = URI.create(uri_format + "/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getTaskById(taskId)), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getEpicByIdTest() {
        int epicId = manager.createNewEpic(epic);
        URI uri = URI.create(uri_format + "/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getEpicById(epicId)), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getSubTaskByIdTest() {
        manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        URI uri = URI.create(uri_format + "/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getSubTaskById(subTaskId)), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void getHistoryTest() {
        int epicId = manager.createNewEpic(epic);
        int subTaskId = manager.createNewSubTask(subTask);
        int taskId = manager.createNewTask(task);
        manager.getTaskById(taskId);
        manager.getSubTaskById(subTaskId);
        manager.getEpicById(epicId);
        URI uri = URI.create(uri_format + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(gson.toJson(manager.getHistory()), response.body());
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Во время теста что-то пошло не так", e);
        }
    }

    @Test
    public void loadFromServerTest() {
        int epicId = manager.createNewEpic(epic);
        int subtaskId = manager.createNewSubTask(subTask);
        int taskId = manager.createNewTask(task);
        manager.getTaskById(taskId);
        manager.getSubTaskById(subtaskId);

        HttpTaskManager loadManager = HttpTaskManager.loadFromServer(URI.create("http://localhost:8080"), gson, kvClient);
        System.out.println(manager.getEpicById(epicId));
        System.out.println(loadManager.getEpicById(epicId));
        assertEquals(manager.getEpicById(epicId), loadManager.getEpicById(epicId));
        assertEquals(manager.getHistory(), loadManager.getHistory());
        assertEquals(manager.getPrioritizedTasks(), loadManager.getPrioritizedTasks());
    }
}
