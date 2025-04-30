package server;

import com.sun.net.httpserver.HttpServer;

import manager.FileBackedTaskManager;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.file.Path;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {

        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));

    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер на порту " + PORT + " остановлен.");
    }

    public static void main(String[] args) throws IOException {
        Path path = Path.of("resultTask.csv");
        File file = new File(path.toString());

        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager taskManager = new FileBackedTaskManager(historyManager, file);
        FileBackedTaskManager manager = taskManager.loadFromFile(file);

        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }

}