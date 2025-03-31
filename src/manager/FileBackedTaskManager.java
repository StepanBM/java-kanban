package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.io.IOException;

import taskData.TaskStatus;
import taskData.TypesTasks;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public void save() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,epic\n");

            for (Task task : outputAllTask()) {
                bw.write(toString(task) + "\n");
            }
            for (Epic epic : outputAllEpic()) {
                bw.write(toString(epic) + "\n");
            }
            for (Subtask subtask : outputAllSubtask()) {
                bw.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            // Пропускаем заголовок csv файла
            String header = reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                Task task =fromString(line);
                manager.createTask(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + e);
        }
        return manager;
    }

    private String toString(Task task) {
        String[] arrayTask = {
                Integer.toString(task.getId()),
                getType(task).toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
        };
        return String.join(",", arrayTask);
    }

    private TypesTasks getType(Task task) {
        if (task instanceof Epic) {
            return TypesTasks.EPIC;
        } else if (task instanceof Subtask) {
            return TypesTasks.SUBTASK;
        } else {
            return TypesTasks.TASK;
        }
    }

    public static Task fromString(String value) {
        String[] params = value.split(",");
        int id = Integer.parseInt(params[0]);
        String typeTasks = params[1];
        String name = params[2];
        String description =  params[4];
        TaskStatus status = TaskStatus.valueOf(params[3]);

        Task task;

       if (typeTasks.equals("TASK")) {
           task = new Task(name, description, status);
       } else if (typeTasks.equals("EPIC")) {
           task = new Epic(name, description, status);
       } else {
           task = new Subtask(name, description, status);
       }
       task.setId(id);
        return task;
    }

    @Override
    public int createTask(Task task) {
        int savedTask = super.createTask(task);
        save();

        return savedTask;
    }

    @Override
    public int createEpic(Epic epic) {
        int savedEpic = super.createEpic(epic);
        save();

        return savedEpic;
    }

    public int createSubtask(int id, Subtask subtask) {
        int savedSubtask = super.createSubtask(id, subtask);
        save();

        return savedSubtask;
    }

    @Override
    public void deleteByIdTask(int id) {
        super.deleteByIdTask(id);
        save();
    }

    @Override
    public void deleteByIdEpic(int id) {
        super.deleteByIdEpic(id);
        save();
    }

    @Override
    public void deleteByIdSubtask(int id) {
        super.deleteByIdSubtask(id);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();

    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public Task getByIdTask(int id) {
        Task task = super.getByIdTask(id);
        return task;

    }

    @Override
    public Epic getByIdEpic(int id) {
        Epic epic = super.getByIdEpic(id);
        return epic;

    }

    @Override
    public Subtask getByIdSubtask(int id) {
        Subtask subtask = super.getByIdSubtask(id);
        return subtask;

    }

    // Обновление задач
    @Override
    public Task updateTask(int id, Task task) {
        Task tasks = super.updateTask(id, task);
        return tasks;
    }

    @Override
    public Epic updateEpic(int id, Epic epic) {
        Epic epics = super.updateEpic(id, epic);
        return epics;
    }

    @Override
    public Subtask updateSubtask(int id, Subtask subtask) {
        Subtask subtasks = super.updateSubtask(id, subtask);
        return subtasks;
    }

}
