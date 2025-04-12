package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import java.io.File;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.io.IOException;

import data.TaskStatus.TaskStatus;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;
    public static String TASK_FIELDS = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public void save() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write(TASK_FIELDS);

            for (Task task : outputAllTask()) {
                bw.write(toString(task) + "\n");
            }
            for (Epic epic : outputAllEpic()) {
                bw.write(toString(epic) + "\n");
            }
            for (Subtask subtask : outputAllSubtask()) {
                bw.write(toString(subtask) + "\n");
            }
            saveHistoryTask(bw);
        } catch (IOException e) {
            throw new ManagerSaveException("Не получилось сохранить в файл " + e);
        }
    }

    private void saveHistoryTask(BufferedWriter bw) {
        try {
            List<Task> historyTask = getHistory();
            bw.write("historyTask:");
            for (Task task : historyTask) {
                bw.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не получилось сохранить в файл " + e);
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager manager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                // Пропускаем заголовок csv файла
                if (line.equals((TASK_FIELDS.trim()))) continue;

                if (line.startsWith("historyTask:")) {
                    String[] ids = line.substring("historyTask:".length()).split(",");
                    for (String id : ids) {
                        if (!id.trim().isEmpty()) {
                            manager.getByIdTask(Integer.parseInt(id));
                            manager.getByIdEpic(Integer.parseInt(id));
                            manager.getByIdSubtask(Integer.parseInt(id));
                        }
                    }
                    continue;
                }

                Task task = fromString(line);

                if (task.getType().toString().equals("EPIC")) {
                    Epic taskEpic = (Epic) task;
                    manager.epics.put(taskEpic.getId(), taskEpic);
                } else if (task.getType().toString().equals("SUBTASK")) {
                    Subtask subtask = (Subtask) task;
                    int epicId = subtask.getepicID();
                    if (manager.epics.containsKey(epicId)) {
                        manager.subtasks.put(subtask.getId(), subtask);
                    }
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не получилось сохранить в файл " + e);
        }
        return manager;
    }

    private String toString(Task task) {
        String[] arrayTask = {
                Integer.toString(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                getEpicId(task),
        };
        return String.join(",", arrayTask);
    }

    private String getEpicId(Task task) {
        if (task.getType().toString().equals("SUBTASK")) {
            return Integer.toString(((Subtask) task).getepicID());
        }
        return "";
    }

    public static Task fromString(String value) {
        String[] params = value.split(",");
        int id = Integer.parseInt(params[0]);
        String typeTasks = params[1];
        String name = params[2];
        String description = params[4];
        TaskStatus status = TaskStatus.valueOf(params[3]);

        Task task;

        if (typeTasks.equals("TASK")) {
            task = new Task(name, description, status);
        } else if (typeTasks.equals("EPIC")) {
            task = new Epic(name, description, status);
        } else {
            int epicId = Integer.parseInt(params[5]);
            task = new Subtask(name, description, status, epicId);
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

    public int createSubtask(Subtask subtask) {
        int savedSubtask = super.createSubtask(subtask);
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
        save();
        return task;

    }

    @Override
    public Epic getByIdEpic(int id) {
        Epic epic = super.getByIdEpic(id);
        save();
        return epic;

    }

    @Override
    public Subtask getByIdSubtask(int id) {
        Subtask subtask = super.getByIdSubtask(id);
        save();
        return subtask;

    }

    @Override
    public Task updateTask(int id, Task task) {
        Task tasks = super.updateTask(id, task);
        save();
        return tasks;
    }

    @Override
    public Epic updateEpic(int id, Epic epic) {
        Epic epics = super.updateEpic(id, epic);
        save();
        return epics;
    }

    @Override
    public Subtask updateSubtask(int id, Subtask subtask) {
        Subtask subtasks = super.updateSubtask(id, subtask);
        save();
        return subtasks;
    }

}
