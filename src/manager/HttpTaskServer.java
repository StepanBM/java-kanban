package manager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import exceptions.ManagerErrorSaveTaskTime;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer extends BaseHttpHandler {

    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {

        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/epics", new TasksHandler());
        server.createContext("/subtasks", new TasksHandler());
        server.createContext("/history", new TasksHandler());
        server.createContext("/prioritized", new TasksHandler());

    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер на порту " + PORT + " остановлен.");
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка запроса от клиента.");

            try {
                URI requestUri = exchange.getRequestURI();
                String path = requestUri.getPath();
                String[] pathParts = path.split("/");
                String requestMethod = exchange.getRequestMethod();
                InputStream inputStreamTask = exchange.getRequestBody();
                String body = new String(inputStreamTask.readAllBytes(), StandardCharsets.UTF_8);

                switch (requestMethod) {
                    case "POST":
                        if (pathParts[1].equals("tasks")) {
                            Task task = gson.fromJson(body, Task.class);
                            try {
                                if (!(body.contains("id")) || task.getId() == 0) {
                                    taskManager.createTask(task);
                                    sendText(exchange, "Задача добавлена", 201);
                                    break;
                                }
                                taskManager.updateTask(task);
                                sendText(exchange, "Задача обновлена", 201);
                            } catch (ManagerErrorSaveTaskTime e) {
                                sendNotAcceptable(exchange, e.getMessage());
                            }

                        } else if (pathParts[1].equals("epics")) {
                            Epic epic = gson.fromJson(body, Epic.class);
                            try {
                                if (!(body.contains("id")) || epic.getId() == 0) {
                                    taskManager.createEpic(epic);
                                    sendText(exchange, "Эпик добавлен", 201);
                                    break;
                                }
                                taskManager.updateEpic(epic);
                                sendText(exchange, "Эпик обновлен", 201);
                            } catch (ManagerErrorSaveTaskTime e) {
                                sendNotAcceptable(exchange, e.getMessage());
                            }

                        } else if (pathParts[1].equals("subtasks")) {
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            try {
                                if (!(body.contains("id")) || subtask.getId() == 0) {
                                    taskManager.createSubtask(subtask);
                                    sendText(exchange, "Подзадача добавлена", 201);
                                    break;
                                }
                                taskManager.updateSubtask(subtask);
                                sendText(exchange, "Подзадача обновлена", 201);
                            } catch (ManagerErrorSaveTaskTime e) {
                                sendNotAcceptable(exchange, e.getMessage());
                            }
                        }
                        break;

                    case "DELETE":
                        IdRequest idRequest = gson.fromJson(body, IdRequest.class);
                        if (idRequest == null) {
                            if (pathParts[1].equals("tasks")) {
                                taskManager.deleteAllTask();
                                sendText(exchange, "Все задачи удалены", 200);

                            } else if (pathParts[1].equals("epics")) {
                                taskManager.deleteAllEpic();
                                sendText(exchange, "Все эпики удалены", 200);

                            } else if (pathParts[1].equals("subtasks")) {
                                taskManager.deleteAllSubtask();
                                sendText(exchange, "Все подзадачи удалены", 200);
                            }
                            break;
                        }

                        int idTask = idRequest.getId();
                        if (pathParts[1].equals("tasks")) {
                            taskManager.deleteByIdTask(idTask);
                            sendText(exchange, "Задача удалена", 200);
                        } else if (pathParts[1].equals("epics")) {
                            taskManager.deleteByIdEpic(idTask);
                            sendText(exchange, "Эпик удален", 200);
                        } else if (pathParts[1].equals("subtasks")) {
                            taskManager.deleteByIdSubtask(idTask);
                            sendText(exchange, "Подзадача удалена", 200);
                        }
                        break;

                    case "GET":
                        IdRequest idRequestGet = gson.fromJson(body, IdRequest.class);
                        if (idRequestGet == null) {
                            if (pathParts[1].equals("tasks")) {
                                sendText(exchange, gson.toJson(taskManager.outputAllTask()), 200);
                            } else if (pathParts[1].equals("epics")) {
                                sendText(exchange, gson.toJson(taskManager.outputAllEpic()), 200);
                            } else if (pathParts[1].equals("subtasks")) {
                                sendText(exchange, gson.toJson(taskManager.outputAllSubtask()), 200);
                            } else if (pathParts[1].equals("history")) {
                                sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
                            } else if (pathParts[1].equals("prioritized")) {
                                sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
                            }
                            break;
                        }

                        int idTaskGet = idRequestGet.getId();
                        if (pathParts[1].equals("tasks")) {
                            Task task = taskManager.getByIdTask(idTaskGet);
                            if (task != null || idTaskGet>0) {
                                sendText(exchange, gson.toJson(taskManager.getByIdTask(idTaskGet)), 200);
                            } else {
                                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
                            }

                        } else if (pathParts[1].equals("epics")) {
                            Epic epic = taskManager.getByIdEpic(idTaskGet);
                            if (epic != null || idTaskGet>0) {
                                sendText(exchange, gson.toJson(taskManager.getByIdEpic(idTaskGet)), 200);
                            } else {
                                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
                            }

                        } else if (pathParts[1].equals("subtasks")) {
                            Subtask subtask = taskManager.getByIdSubtask(idTaskGet);
                            if (subtask != null || idTaskGet>0) {
                                sendText(exchange, gson.toJson(taskManager.getByIdSubtask(idTaskGet)), 200);
                            } else {
                                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
                            }
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange, "Internal Server Error");
            } finally {
                exchange.close();
            }
        }
    }

    public static class IdRequest {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}