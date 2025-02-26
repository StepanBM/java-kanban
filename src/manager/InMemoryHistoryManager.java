package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyList = new ArrayList<>();

   public List<Task> getHistory() {
        return new ArrayList<>(historyList); // возвращаем копию списка
    }

    // Метод по контролю за количеством задач сохраненных в истории
    public void add(Task task) {
        historyList.add(task);
        if (historyList.size()>10) {
            historyList.remove(0);
        }
    }
}
