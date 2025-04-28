import com.google.gson.Gson;
import data.TaskStatus.TaskStatus;
import manager.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager(historyManager);
        taskServer = new HttpTaskServer(manager);
        gson = Managers.getGson();

        // Очищаем все задачи, эпики и подзадачи перед каждым тестом
        manager.deleteAllTask();
        manager.deleteAllEpic();
        manager.deleteAllSubtask();

        taskServer.start(); // Запускаем сервер
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop(); // Останавливаем сервер после каждого теста
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 33));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем REST, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.outputAllTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя подзадачи №1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }
}