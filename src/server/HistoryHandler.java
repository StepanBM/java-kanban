package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    protected void processGet(HttpExchange exchange, String body) throws IOException {
        IdRequest idRequestGet = gson.fromJson(body, IdRequest.class);
        if (idRequestGet == null) {
            sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
        }
    }

}
