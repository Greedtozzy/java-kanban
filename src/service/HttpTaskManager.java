package service;

import client.KVTaskClient;
import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    protected final URI uri;
    protected final Gson gson = Managers.getGson();

    protected final KVTaskClient client = new KVTaskClient();
    public HttpTaskManager(URI uri) {
        super();
        this.uri = uri;
    }

    @Override
    protected void save() {
        for (Task task : tasks.values()) client.put("tasks", gson.toJson(task));
        for (Epic epic : epics.values()) client.put("epics", gson.toJson(epic));
        for (SubTask subTask : subTasks.values()) client.put("subtasks", gson.toJson(subTask));
        for (Task task : getHistory()) client.put("history", gson.toJson(task));
    }

    public static HttpTaskManager loadFromServer(URI uri, Gson gson, KVTaskClient client) {
        HttpTaskManager manager = new HttpTaskManager(uri);
        List<String> tasksList = gson.fromJson(client.load("tasks"), ArrayList.class);
        for (String strTask : tasksList) {
            Task task = gson.fromJson(strTask, Task.class);
            manager.tasks.put(task.getId(), task);
        }

        List<String> epicsList = gson.fromJson(client.load("epics"), ArrayList.class);
        for (String strEpic : epicsList) {
            Epic epic = gson.fromJson(strEpic, Epic.class);
            manager.epics.put(epic.getId(), epic);
        }

        String line = client.load("subtasks");
        List<String> subTasksList = gson.fromJson(client.load("subtasks"), ArrayList.class);
        for (String strSubTask : subTasksList) {
            SubTask subTask = gson.fromJson(strSubTask, SubTask.class);
            manager.subTasks.put(subTask.getId(), subTask);
        }

        List<String> history = gson.fromJson(client.load("history"), ArrayList.class);
        for (String strTask : history) {
            if (strTask.contains("epicId")) {
                SubTask subTask = gson.fromJson(strTask, SubTask.class);
                manager.historyManager.addTaskInHistory(subTask);
            } else if (strTask.contains("subTasksIds")) {
                Epic epic = gson.fromJson(strTask, Epic.class);
                manager.historyManager.addTaskInHistory(epic);
            } else {
                Task task = gson.fromJson(strTask, Task.class);
                manager.historyManager.addTaskInHistory(task);
            }
        }

        manager.sortedTasks.addAll(manager.tasks.values());
        manager.sortedTasks.addAll(manager.subTasks.values());

        return manager;
    }
}
