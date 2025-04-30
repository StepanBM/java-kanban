package server;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerErrorSaveTaskTime;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void processGet(HttpExchange exchange, String body) throws IOException {
        IdRequest idRequestGet = gson.fromJson(body, IdRequest.class);
        if (idRequestGet == null) {
            sendText(exchange, gson.toJson(taskManager.outputAllTask()), 200);
        } else {
            int idTaskGet = idRequestGet.getId();
            Subtask subtask = taskManager.getByIdSubtask(idTaskGet);
            if (subtask != null || idTaskGet > 0) {
                sendText(exchange, gson.toJson(taskManager.getByIdSubtask(idTaskGet)), 200);
            } else {
                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
            }
        }

    }

    @Override
    protected void processPost(HttpExchange exchange, String body) throws IOException {
        Subtask subtask = gson.fromJson(body, Subtask.class);
        try {
            if (!(body.contains("id")) || subtask.getId() == 0) {
                taskManager.createSubtask(subtask);
                sendText(exchange, "Задача добавлена", 201);
            } else {
                taskManager.updateSubtask(subtask);
                sendText(exchange, "Задача обновлена", 201);
            }
        } catch (ManagerErrorSaveTaskTime e) {
            sendNotAcceptable(exchange, e.getMessage());
        }
    }

    @Override
    protected void processDelete(HttpExchange exchange, String body) throws IOException {
        IdRequest idRequest = gson.fromJson(body, IdRequest.class);
        if (idRequest == null) {
            taskManager.deleteAllSubtask();
            sendText(exchange, "Все подзадачи удалены", 200);
        } else {
            int idTask = idRequest.getId();
            taskManager.deleteByIdSubtask(idTask);
            sendText(exchange, "Подзадача удалена", 200);
        }
    }

}
