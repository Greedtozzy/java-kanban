package service;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonSubTasks = gson.toJson(new ArrayList<>(subTasks.values()));
        client.put("subtasks", jsonSubTasks);
        List<Integer> history = new ArrayList<>();
        for (Task task : getHistory()) history.add(task.getId());
        String jsonHistory = gson.toJson(history);
        client.put("history", jsonHistory);
    }

    public static HttpTaskManager loadFromServer(URI uri, Gson gson, KVTaskClient client) {
        HttpTaskManager manager = new HttpTaskManager(uri);
        List<Task> tasksList = gson.fromJson(client.load("tasks"),
                new TypeToken<ArrayList<Task>>() {}.getType());
        for (Task task : tasksList) {
            manager.tasks.put(task.getId(), task);
        }

        List<Epic> epicsList = gson.fromJson(client.load("epics"),
                new TypeToken<ArrayList<Epic>>() {}.getType());
        for (Epic epic : epicsList) {
            manager.epics.put(epic.getId(), epic);
        }

        List<SubTask> subTasksList = gson.fromJson(client.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {}.getType());
        for (SubTask subTask : subTasksList) {
            manager.subTasks.put(subTask.getId(), subTask);
        }

        List<Integer> history = gson.fromJson(client.load("history"),
                new TypeToken<ArrayList<Integer>>() {}.getType());
        for (int id : history) manager.historyManager.addTaskInHistory(manager.findTask(id));

        manager.sortedTasks.addAll(manager.tasks.values());
        manager.sortedTasks.addAll(manager.subTasks.values());

        return manager;
    }
}
