import manager.*;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    HistoryManager historyManager = Managers.getDefaultHistory();

    @BeforeEach
    public void toBegin() {
        super.taskManager = new InMemoryTaskManager(historyManager);
        createTaskEpicSubtask();
    }

}