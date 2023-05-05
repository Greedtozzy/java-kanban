package server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.RequestMethod;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandle extends Handle implements HttpHandler {
    public HistoryHandle(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());
        String path = exchange.getRequestURI().getPath();
        if (!requestMethod.equals(RequestMethod.GET)) {
            send(exchange, "Данный метод не поддерживается = " + requestMethod, 404);
            return;
        }
        if (!path.equals("/tasks/history")) {
            send(exchange, "Данный путь не поддерживается = " + path, 404);
            return;
        }
        String response = gson.toJson(manager.getHistory());
        send(exchange, response, 200);
    }
}
