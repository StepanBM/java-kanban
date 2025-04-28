package manager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
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
      //  server.createContext("/epics", new EpicsHandler());
     //   server.createContext("/subtasks", new SubtasksHandler());
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
                            if (taskManager.theTaskIntersectsInTheList(task)) {
                                sendNotAcceptable(exchange,"Не получилось сохранить задачу, измените время начала");

                            }
                           else if (!(body.contains("id")) || task.getId()==0) {
                                taskManager.createTask(task);
                                sendText(exchange, "Задача добавлена", 201);
                                break;
                            }
                           // int taskId = task.getId();
                            taskManager.updateTask(task);
                            sendText(exchange, "Задача обновлена", 201);
                        } else if (pathParts[1].equals("epics")) {
                            Epic epic = gson.fromJson(body, Epic.class);
                            if (taskManager.theTaskIntersectsInTheList(epic)) {
                                sendNotAcceptable(exchange,"Не получилось сохранить задачу, измените время начала");

                            }
                           else if (!(body.contains("id"))) {
                                taskManager.createEpic(epic);
                                sendText(exchange, "Эпик добавлен", 201);
                                break;
                            }
                          //  int epicId = epic.getId();
                            taskManager.updateEpic(epic);
                            sendText(exchange, "Эпик обновлен", 201);
                        } else {
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            if (taskManager.theTaskIntersectsInTheList(subtask)) {
                                sendNotAcceptable(exchange,"Не получилось сохранить задачу, измените время начала");
                            }
                           else if (!(body.contains("id"))) {
                                taskManager.createSubtask(subtask);
                                sendText(exchange, "Подзадача добавлена", 201);
                                break;
                            }
                          //  int subtaskId = subtask.getId();
                            taskManager.updateSubtask(subtask);
                            sendText(exchange, "Подзадача обновлена", 201);
                        }
                        break;
                    case "DELETE":
                            IdRequest idRequest = gson.fromJson(body, IdRequest.class);
                            if (idRequest==null) {
                                if (pathParts[1].equals("tasks")) {
                                  taskManager.deleteAllTask();
                                        sendText(exchange, "Все задачи удалены", 200);
                                } else if (pathParts[1].equals("epics")) {
                                        taskManager.deleteAllEpic();
                                        sendText(exchange, "Все эпики удалены", 200);
                                } else {
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
                        } else {
                                taskManager.deleteByIdSubtask(idTask);
                                sendText(exchange, "Подзадача удалена", 200);
                        }

                        break;
                    case "GET":
                        IdRequest idRequestGet = gson.fromJson(body, IdRequest.class);
                        if (idRequestGet==null) {
                            if (pathParts[1].equals("tasks")) {
                                sendText(exchange, gson.toJson(taskManager.outputAllTask()), 200);
                            } else if (pathParts[1].equals("epics")) {
                                sendText(exchange, gson.toJson(taskManager.outputAllEpic()), 200);
                            } else if (pathParts[1].equals("subtasks")) {
                                sendText(exchange, gson.toJson(taskManager.outputAllSubtask()), 200);
                            } else if (pathParts[1].equals("history")) {
                                sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
                            } else {
                                sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
                            }
                            break;
                        }
                        int idTaskGet = idRequestGet.getId();
                        if (pathParts[1].equals("tasks")) {
                           Task task = taskManager.getByIdTask(idTaskGet);
                           if (task != null) {
                               sendText(exchange, gson.toJson(taskManager.getByIdTask(idTaskGet)), 200);
                           } else {
                               sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
                           }
                        } else if (pathParts[1].equals("epics")) {
                            Epic epic = taskManager.getByIdEpic(idTaskGet);
                            if (epic != null) {
                                sendText(exchange, gson.toJson(taskManager.getByIdEpic(idTaskGet)), 200);
                            } else {
                                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
                            }
                        } else if (pathParts[1].equals("subtasks")) {
                            Subtask subtask = taskManager.getByIdSubtask(idTaskGet);
                            if ( subtask != null) {
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

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFoundId(HttpExchange h, String error404) throws IOException {
        byte[] resp = error404.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotAcceptable(HttpExchange h, String error406) throws IOException {
        byte[] resp = error406.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendInternalServerError(HttpExchange h, String error500) throws IOException {
        byte[] resp = error500.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(500, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public class IdRequest {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}