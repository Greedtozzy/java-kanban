package server.handles;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.RequestMethod;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Handle implements HttpHandler {
    protected final TaskManager manager;
    protected final Gson gson;

    public Handle(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());
        if (!requestMethod.equals(RequestMethod.GET)) {
            send(exchange, "Метод = " + requestMethod, 404);
            return;
        }
        String response = gson.toJson(manager.getPrioritizedTasks());
        send(exchange, response, 200);
    }

    protected void send(HttpExchange exchange, String body, int code) throws IOException {
        byte[] responseBody = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, responseBody.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        } finally {
            exchange.close();
        }
    }

    protected int parseIntId(String query) {
        if (query == null) {
            return -1;
        }
        try {
            String id = query.substring(3);
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
