package manager;

public class Managers {
   // private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
