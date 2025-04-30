package server;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerErrorSaveTaskTime;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    protected void processPost(HttpExchange exchange, String body) throws IOException {
        Epic epic = gson.fromJson(body, Epic.class);
        try {
            if (!(body.contains("id")) || epic.getId() == 0) {
                taskManager.createEpic(epic);
                sendText(exchange, "Эпик добавлен", 201);
            } else {
                taskManager.updateEpic(epic);
                sendText(exchange, "Эпик обновлен", 201);
            }
        } catch (ManagerErrorSaveTaskTime | IOException e) {
            sendNotAcceptable(exchange, e.getMessage());
        }
    }

    protected void processGet(HttpExchange exchange, String body) throws IOException {
        IdRequest idRequestGet = gson.fromJson(body, IdRequest.class);
        if (idRequestGet == null) {
            sendText(exchange, gson.toJson(taskManager.outputAllEpic()), 200);
        } else {
            int idTaskGet = idRequestGet.getId();
            Epic epic = taskManager.getByIdEpic(idTaskGet);
            if (epic != null || idTaskGet > 0) {
                sendText(exchange, gson.toJson(taskManager.getByIdEpic(idTaskGet)), 200);
            } else {
                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
            }
        }
    }

    protected void processDelete(HttpExchange exchange, String body) throws IOException {
        IdRequest idRequest = gson.fromJson(body, IdRequest.class);
        if (idRequest == null) {
            taskManager.deleteAllEpic();
            sendText(exchange, "Все эпики удалены", 200);
        } else {
            int idTask = idRequest.getId();
            taskManager.deleteByIdEpic(idTask);
            sendText(exchange, "Эпик удален", 200);
        }
    }

}
