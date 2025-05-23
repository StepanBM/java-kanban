package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> historyMap = new HashMap<>();

    public List<Task> getHistory() {
        return getTasks(); // возвращаем копию списка
    }

    // Метод по контролю за количеством задач сохраненных в истории
    @Override
    public Task addTask(Task task) {
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }
       return linkLast(task);
    }

    @Override
    public void removeTask(int id) {
        Node node = historyMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    // Первый элемент списка
    private Node head;
    // Последний элемент списка
    private Node tail;

    //Добавление задачив конец списка
    public Task linkLast(Task task) {

        // Создаем новый узел
        Node<Task> newNode = new Node<>(tail, task, null);
        if (tail == null) {
            // Список был пустым, значит новый узел становится головой (началом)
            head = newNode;
        } else {
            // Соединяем старый последний элемент с новым
            tail.next = newNode;
        }
        // Обновляем хвост
        tail = newNode;
        historyMap.put(task.getId(), newNode);

        return task;
    }

    // Собирание всех задач из списка в обычный ArrayList
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> element = head;
        while (element != null) {
            tasks.add(element.data);
            element = element.next;
        }
        return tasks;
    }

    public void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

}
