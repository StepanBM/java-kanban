import com.google.gson.Gson;

import data.TaskStatus.TaskStatus;

import manager.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Имя задачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 33));

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
        assertEquals("Имя задачи №1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);

        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        List<Epic> epicsFromManager = manager.outputAllEpic();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя эпика №1", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3332, 7, 1, 0, 33), 1);

        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        List<Subtask> subtasksFromManager = manager.outputAllSubtask();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя подзадачи №1", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task("Имя задачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1999, 7, 1, 0, 33));
        manager.createTask(task1);

        Task task = new Task(1, "Имя задачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 33));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/id");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        List<Task> tasksFromManager = manager.outputAllTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя задачи №1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        manager.createEpic(epic1);

        Epic epic = new Epic(1, "Имя эпика №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);

        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();

        URI url = URI.create("http://localhost:8080/epics/id");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        List<Epic> epicsFromManager = manager.outputAllEpic();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя эпика №2", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3332, 7, 1, 0, 33), 1);
        manager.createSubtask(subtask1);

        Subtask subtask = new Subtask(2, "Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3330, 7, 1, 0, 33), 1);

        String epicJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/id");

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        List<Subtask> epicsFromManager = manager.outputAllSubtask();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя подзадачи №2", epicsFromManager.get(0).getName(), "Некорректное имя задачи");

    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Имя задачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1999, 7, 1, 0, 33));
        Task task1 = new Task("Имя задачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1998, 7, 1, 0, 33));
        manager.createTask(task);
        manager.createTask(task1);

        int taskId = task.getId();
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(taskId);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/id"); // Убедитесь, что путь правильный
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        List<Task> tasksFromManager = manager.outputAllTask();

        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertFalse(tasksFromManager.contains(task), "Задача не была удалена");
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {

        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Epic epic1 = new Epic("Имя эпика №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
        manager.createEpic(epic);
        manager.createEpic(epic1);

        int epicId = epic.getId();
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(epicId);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/id"); // Убедитесь, что путь правильный
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        List<Epic> epicsFromManager = manager.outputAllEpic();

        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertFalse(epicsFromManager.contains(epic), "Задача не была удалена");
    }

    @Test
    public void testDeleteSubtaskById() throws IOException, InterruptedException {

        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1998, 7, 1, 0, 33), 1);
        Subtask subtask1 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1991, 7, 1, 0, 33), 1);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask1);

        int subtaskId = subtask.getId();
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(subtaskId);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/id"); // Убедитесь, что путь правильный
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        List<Subtask> subtasksFromManager = manager.outputAllSubtask();

        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertFalse(subtasksFromManager.contains(epic), "Задача не была удалена");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1999, 7, 1, 0, 33));
        manager.createTask(task);

        int taskId = task.getId();

        URI url = URI.create("http://localhost:8080/tasks/id");
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(taskId);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("GET", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        Task retrievedTask = gson.fromJson(response.body(), Task.class);

        assertEquals(task.getId(), retrievedTask.getId(), "id задачи истории должен совпадать");
        assertEquals(task.getName(), retrievedTask.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 2", TaskStatus.NEW);
        manager.createEpic(epic);

        int epicId = epic.getId();

        URI url = URI.create("http://localhost:8080/epics/id");
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(epicId);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("GET", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        Epic retrievedEpic = gson.fromJson(response.body(), Epic.class);

        assertNotNull(retrievedEpic, "Эпик не получен");
        assertEquals(epic.getName(), retrievedEpic.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя эпика №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1998, 7, 1, 0, 33), 1);
        Subtask subtask1 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1991, 7, 1, 0, 33), 1);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask1);

        int subtaskId = subtask.getId();

        URI url = URI.create("http://localhost:8080/subtasks/id");
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(subtaskId);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("GET", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        Subtask retrievedSubtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(subtask.getId(), retrievedSubtask.getId(), "id задачи истории должен совпадать");
        assertEquals(subtask.getName(), retrievedSubtask.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 33));
        manager.createTask(task);

        int taskId = task.getId();
        manager.getByIdTask(taskId);

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        Task[] historyTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, historyTasks.length, "Некорректное количество задач");
        assertEquals(taskId, historyTasks[0].getId(), "id задачи истории должен совпадать с просмотренной задачей");
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 33));
        Task task2 = new Task("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(5555, 7, 1, 0, 33));

        manager.createTask(task1);
        manager.createTask(task2);

        URI url = URI.create("http://localhost:8080/prioritized");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);

        assertEquals(2, prioritizedTasks.length, "Некорректное количество задач");
        assertEquals(task1.getId(), prioritizedTasks[0].getId(), "id задачи истории должен совпадать с добавленной задачей");
        assertEquals(task2.getId(), prioritizedTasks[1].getId(), "id задачи истории должен совпадать с добавленной задачей");
    }

    @Test
    public void testGetTaskByIdError404() throws IOException, InterruptedException {
        Task task = new Task("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1999, 7, 1, 0, 33));
        manager.createTask(task);

        int taskId = -1;

        URI url = URI.create("http://localhost:8080/tasks/id");
        HttpTaskServer.IdRequest idRequest = new HttpTaskServer.IdRequest();
        idRequest.setId(taskId);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method("GET", HttpRequest.BodyPublishers.ofString(gson.toJson(idRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Некорректный статус ответа");
    }

    @Test
    public void testGetTaskByIdError406() throws IOException, InterruptedException {
        Task task = new Task("Имя задачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 33));
        Task task1 = new Task("Имя задачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(3333, 7, 1, 0, 35));
        manager.createTask(task1);

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Некорректный статус ответа");
    }

}