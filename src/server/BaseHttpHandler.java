package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerErrorSaveTaskTime;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    protected Gson gson = Managers.getGson();
    protected TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка запроса от клиента.");

            try {
                String requestMethod = exchange.getRequestMethod();
                InputStream inputStreamTask = exchange.getRequestBody();
                String body = new String(inputStreamTask.readAllBytes(), StandardCharsets.UTF_8);

                switch (requestMethod) {
                    case "GET":
                        processGet(exchange, body);
                        break;
                    case "POST":
                        processPost(exchange, body);
                        break;
                    case "DELETE":
                        processDelete(exchange, body);
                        break;
                    default:
                        sendMethodNotAllowed(exchange, "GET, POST, DELETE");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalServerError(exchange, "Internal Server Error");
            } finally {
                exchange.close();
            }
        }

    protected void processGet(HttpExchange exchange, String body) throws IOException {
    }

    protected void processPost(HttpExchange exchange, String body) throws IOException {
    }

    protected void processDelete(HttpExchange exchange, String body) throws IOException {
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

    protected void sendMethodNotAllowed(HttpExchange h, String error405) throws IOException {
        byte[] resp = error405.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(405, resp.length);
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

}
