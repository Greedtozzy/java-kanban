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
        final Node node = nodes.remove(id);
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            head = node.next;
            if (head == null) {
                tail = null;
            } else {
                tail.prev = null;
            }
        }
    }
    // Метод, удаляющий задачу из истории просмотров.

    @Override
    public void addTaskInHistory(Task task) {
        if (task != null) {
            if (nodes.containsKey(task.getId())) {
                remove(task.getId());
            }
            linkLast(task);
        }
    }
    // Метод, добавляющий задачу в список. Используется после обращения к ней. Так-же проверяет длину списка.

    private void linkLast(Task task) {
        final Node<Task> node = new Node<>(tail, task, null);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        nodes.put(task.getId(), node);
    }
    // Метод, добавляющий новый узел в мапу истории.

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }
    // Метод, заливающий значения мапы в список.

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
