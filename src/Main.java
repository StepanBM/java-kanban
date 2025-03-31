import taskType.TaskStatus;
import tasks.Epic;
import tasks.Subtask;

import manager.Managers;

import java.util.Scanner;

import manager.FileBackedTaskManager;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        HistoryManager historyManager = Managers.getDefaultHistory();
//        TaskManager manager = Managers.getDefault(historyManager);

        Path path = Path.of("result.csv");
        File file = new File(path.toString());
       FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);

       String stringTask = "1,TASK,Task1,NEW,Description,task1, ";

       // Создаем задачу из строки
        Task task = manager.fromString(stringTask);

        // Преобразование задачи обратно в строку
        String stringTaskReverse = task.toString();
        System.out.println(stringTaskReverse);

        while (true) {
            printMenu();
            int command = scanner.nextInt();
            if (command == 1) {
                scanner.nextLine();
                String command2 = "10";
                while (!command2.equals("0")) {
                    printMenuEpic();
                    command2 = scanner.nextLine();

                    switch (command2) {
                        case "1":
                            System.out.println(manager.outputAllEpic());
                            break;
                        case "2":
                            manager.deleteAllEpic();
                            break;
                        case "3":
                            System.out.println(manager.getByIdEpic(3));
                            break;
                        case "4":
                            Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
                            Epic epic2 = new Epic("Имя №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
                            Epic epic3 = new Epic("Имя №3", "Ооооочень длинное описание № 3", TaskStatus.NEW);
                            Epic epic4 = new Epic("Имя №4", "Ооооочень длинное описание № 4", TaskStatus.NEW);
                            Epic epic5 = new Epic("Имя №5", "Ооооочень длинное описание № 5", TaskStatus.NEW);
                            manager.createEpic(epic1);
                            manager.createEpic(epic2);
                            manager.createEpic(epic3);
                            manager.createEpic(epic4);
                            manager.createEpic(epic5);
                            break;
                        case "5":
                            Epic epic = new Epic("Имя обновленное", "Ооооочень длинное описание", TaskStatus.NEW);
                            manager.updateEpic(2, epic);
                            break;
                        case "6":
                            manager.deleteByIdEpic(3);
                            break;
                        case "7":
                            System.out.println(manager.getAllEpicSubtasks(2));
                            break;
                        case "0":
                            System.out.println("Программа завершилась, до скорой встречи!");
                            break;
                        default:
                            System.out.println("Такой команды пока нет, введите другую, пожалуйста");
                    }


                }
        } else if (command == 2) {
                scanner.nextLine();
                String command3 = "10";
                while (!command3.equals("0")) {
                    printMenuSubtask();
                    command3 = scanner.nextLine();
                    switch (command3) {
                        case "1":
                            System.out.println(manager.outputAllSubtask());
                            break;
                        case "2":
                            manager.deleteAllSubtask();
                            break;
                        case "3":
                            System.out.println(manager.getByIdSubtask(7));
                            break;
                        case "4":
                            Subtask subtask1 = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
                            Subtask subtask2 = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
                            Subtask subtask3 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW);
                            Subtask subtask4 = new Subtask("Имя подзадачи №4", "Ооооочень длинное описание № 4", TaskStatus.NEW);
                            Subtask subtask5 = new Subtask("Имя подзадачи №5", "Ооооочень длинное описание № 5", TaskStatus.NEW);
                            manager.createSubtask(1, subtask1);
                            manager.createSubtask(1, subtask2);
                            manager.createSubtask(3, subtask3);
                            manager.createSubtask(4, subtask4);
                            manager.createSubtask(3, subtask5);
                            break;
                        case "5":
                            Subtask subtask = new Subtask("Имя обновленное", "Ооооочень длинное описание", TaskStatus.NEW);
                            manager.updateSubtask(7, subtask);
                            break;
                        case "6":
                           manager.deleteByIdSubtask(7);
                            break;
                        case "0":
                            System.out.println("Программа завершилась, до скорой встречи!");
                            break;
                        default:
                            System.out.println("Такой команды пока нет, введите другую, пожалуйста");
                    }

                }
            } else if (command == 3) {
                scanner.nextLine();
              System.out.println(manager.getHistory());
            } else {
                System.out.println("Тут ничего нет!!!");
                return;
            }

            for (Task tasks : manager.loadFromFile(file).outputAllTask()) {
                System.out.println(tasks);
            }
    }

    }

    private static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Эпики");
        System.out.println("2 - Подзадачи");
        System.out.println("3 - История просмотров по id");
        System.out.println("0 - Выход");
    }

    private static void printMenuEpic() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Получение списка всех эпиков");
        System.out.println("2 - Удаление всех эпиков");
        System.out.println("3 - Получение эпика по id");
        System.out.println("4 - Создание эпика");
        System.out.println("5 - Обновление эпика");
        System.out.println("6 - Удаление эпика по id");
        System.out.println("7 - Получение списка всех подзадач определённого эпика");
        System.out.println("0 - Выход");
    }

    private static void printMenuSubtask() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Получение списка всех подзадач");
        System.out.println("2 - Удаление всех подзадач"); // !!!!
        System.out.println("3 - Получение подзадачи по id");
        System.out.println("4 - Создание подзадачи");
        System.out.println("5 - Обновление подзадачи"); // !!!
        System.out.println("6 - Удаление подзадачи по id"); // !!!
        System.out.println("0 - Выход");
    }

}
