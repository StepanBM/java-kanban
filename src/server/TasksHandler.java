package server;

import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerErrorSaveTaskTime;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler {


    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void processGet(HttpExchange exchange, String body) throws IOException {
        IdRequest idRequestGet = gson.fromJson(body, IdRequest.class);
        if (idRequestGet == null) {
            sendText(exchange, gson.toJson(taskManager.outputAllTask()), 200);
        } else {
            int idTaskGet = idRequestGet.getId();
            Task task = taskManager.getByIdTask(idTaskGet);
            if (task != null || idTaskGet > 0) {
                sendText(exchange, gson.toJson(taskManager.getByIdTask(idTaskGet)), 200);
            } else {
                sendNotFoundId(exchange, "Данный id: " + idTaskGet + " не найден");
            }
        }

    }

    @Override
    protected void processPost(HttpExchange exchange, String body) throws IOException {
        Task task = gson.fromJson(body, Task.class);
        try {
            if (!(body.contains("id")) || task.getId() == 0) {
                taskManager.createTask(task);
                sendText(exchange, "Задача добавлена", 201);
            } else {
                taskManager.updateTask(task);
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
            taskManager.deleteAllTask();
            sendText(exchange, "Все задачи удалены", 200);
        } else {
            int idTask = idRequest.getId();
            taskManager.deleteByIdTask(idTask);
            sendText(exchange, "Задача удалена", 200);
        }
    }
}



