package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import server.handles.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager manager;


    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new Handle(manager, gson));
        server.createContext("/tasks/task", new TasksHandle(manager, gson));
        server.createContext("/tasks/epic", new EpicsHandle(manager, gson));
        server.createContext("/tasks/subtask", new SubTasksHandle(manager, gson));
        server.createContext("/tasks/history", new HistoryHandle(manager, gson));

    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }
}
