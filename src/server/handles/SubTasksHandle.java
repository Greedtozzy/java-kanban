package server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import server.RequestMethod;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubTasksHandle extends Handle implements HttpHandler {
    public SubTasksHandle(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        String pathPart = path.replaceFirst("/tasks/subtask", "").replaceFirst("/", "");
        switch (requestMethod) {
            case GET:
                if (!pathPart.isBlank()) {
                    if (pathPart.equals("epic/")) {
                        int id = parseIntId(query);
                        if (id != -1) {
                            String response = gson.toJson(manager.getSubTasksByEpicId(id));
                            send(exchange, response, 200);
                        } else {
                            send(exchange, "Не верный формат id = " + query, 404);
                        }
                        return;
                    }
                    send(exchange, "Запрос не доступен - " + path, 404);
                    return;
                }
                if (query == null) {
                    String response = gson.toJson(manager.getListAllSubTasks());
                    send(exchange, response, 200);
                } else {
                    int id = parseIntId(query);
                    if (id != -1) {
                        String response = gson.toJson(manager.getSubTaskById(id));
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
                SubTask subTask = gson.fromJson(new String(is.readAllBytes(), StandardCharsets.UTF_8), SubTask.class);
                int subTaskId = subTask.getId();
                if (subTaskId == 0) {
                    int createId = manager.createNewSubTask(subTask);
                    send(exchange, "Подзадача успешно создана. Id = " + createId, 200);
                } else {
                    manager.updateSubTask(subTask);
                    send(exchange, "Подзадача успешно обновлена. Id = " + subTaskId, 200);
                }
                break;

            case DELETE:
                if (!pathPart.isBlank()) {
                    send(exchange, "Запрос не доступен - " + path, 404);
                    return;
                }
                if (query == null) {
                    manager.deleteAllSubTasks();
                    send(exchange, "Все подзадачи удалены", 200);
                    return;
                }
                int id = parseIntId(query);
                if (id != -1) {
                    manager.deleteSubTaskById(id);
                    send(exchange, "Подзадача с id = " + id + " удалена", 200);
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
