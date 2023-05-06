package server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import server.RequestMethod;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandle extends Handle implements HttpHandler {

    public EpicsHandle(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        String pathPart = path.replaceFirst("/tasks/epic", "").replaceFirst("/", "");
        switch (requestMethod) {
            case GET:
                if (!pathPart.isBlank()) {
                    send(exchange, "Запрос не доступен - " + path, 404);
                    return;
                }
                if (query == null) {
                    String response = gson.toJson(manager.getListAllEpics());
                    send(exchange, response, 200);
                } else {
                    int id = parseIntId(query);
                    if (id != -1) {
                        String response = gson.toJson(manager.getEpicById(id));
                        send(exchange, response, 200);
                    } else {
                        send(exchange, "Не верный формат id = " + query, 404);
                    }
                }
            case POST:
                if (query != null) {
                    send(exchange, "Параметр не допустим = " + query, 404);
                    return;
                }
                InputStream is = exchange.getRequestBody();
                Epic epic = gson.fromJson(new String(is.readAllBytes(), StandardCharsets.UTF_8), Epic.class);
                int epicId = epic.getId();
                if (epicId == 0) {
                    int createdId = manager.createNewEpic(epic);
                    send(exchange, "Эпик успешно создан. Id = " + createdId, 200);
                } else {
                    manager.updateEpic(epic);
                    send(exchange, "Эпик успешно обновлен. Id = " + epicId, 200);
                }
                break;

            case DELETE:
                if (!pathPart.isBlank()) {
                    send(exchange, "Запрос не доступен - " + path, 404);
                    return;
                }
                if (query == null) {
                    manager.deleteAllEpics();
                    send(exchange, "Все эпики удалены", 200);
                    return;
                }
                int id = parseIntId(query);
                if (id != -1) {
                    manager.deleteEpicById(id);
                    send(exchange, "Эпик с id = " + id + " удалена", 200);
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
