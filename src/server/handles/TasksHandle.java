package server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import server.RequestMethod;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandle extends Handle implements HttpHandler {

    public TasksHandle(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        String pathPart = path.replaceFirst("/tasks/task", "").replaceFirst("/", "");
        int id = parseIntId(query);
        switch (requestMethod) {
            case GET:
                if (query == null) {
                    String response = gson.toJson(manager.getListAllTasks());
                    send(exchange, response, 200);
                } else {
                    if (id != -1) {
                        String response = gson.toJson(manager.getTaskById(id));
                        send(exchange, response, 200);
                    } else {
                        send(exchange, "Не верный формат id = " + query, 404);
                    }
                }
                break;

            case POST:
                if (!pathPart.isBlank()) {
                    send(exchange, "Запрос не доступен - " + path, 404);
                    return;
                }
                if (query != null) {
                    send(exchange, "Параметр не допустим = " + query, 404);
                    return;
                }
                InputStream is = exchange.getRequestBody();
                Task task = gson.fromJson(new String(is.readAllBytes(), StandardCharsets.UTF_8), Task.class);
                int taskId = task.getId();
                if (taskId == 0) {
                    int createdId = manager.createNewTask(task);
                    send(exchange, "Задача успешно создана. Id = " + createdId, 200);
                } else {
                    manager.updateTask(task);
                    send(exchange, "Задача успешно обновлена. Id = " + taskId, 200);
                }
                break;


            case DELETE:
                if (!pathPart.isBlank()) {
                    send(exchange, "Запрос не доступен - " + path, 404);
                    return;
                }
                if (query == null) {
                    manager.deleteAllTasks();
                    send(exchange, "Все задачи удалены", 200);
                    return;
                }
                if (id != -1) {
                    manager.deleteTaskById(id);
                    send(exchange, "Задача с id = " + id + " удалена", 200);
                } else {
                    send(exchange, "Не верный формат id = " + query, 404);
                }
                break;

            default:
                send(exchange, "Метод = " + requestMethod, 404);
                break;
        }
    }


}
