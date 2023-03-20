package service;
import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private Map<Integer, Node> nodes = new HashMap<>();
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
    // Метод, выводящий список, в котором хранится история обращений к задачам.

    @Override
    public void remove(int id) {
        if (nodes.containsKey(id)) {
            nodes.remove(id);
        }
    }
    // Метод, удаляющий задачу из истории просмотров.

    @Override
    public void addTaskInHistory(Task task) {
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }
    // Метод, добавляющий задачу в список. Используется после обращения к ней. Так-же проверяет длину списка.

    public void linkLast(Task task) {
        if (task !=null) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            nodes.put(task.getId(), newNode);
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> node : nodes.values()) {
            tasks.add(node.data);
        }
        return tasks;
    }

    class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
