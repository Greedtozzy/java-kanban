/* Доброго времени суток!
На всякий случай сделал меню, чтобы проверить работоспособность приложения.
На вебинаре с наставником задал вопрос по наполнению класса TaskManager.
Получил совет - все методы по работе с задачами(всех типов), писать внутри одного класса.
Была еще идея создать класс Service, из которого были бы наследованы классы, выполняющие однотипные задачи.
Как пример - класс Creator собрал бы в себе все методы по созданию задач.

Еще немного про проверку статуса у эпика. Метод получился большой и страшный.
Сокурсники обсуждали такую штуку, как enum. Но наставник посоветовал оставить её использование до следующего ТЗ=)
 */

import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);


        while (true) {
            menu();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.println("Введите название");
                    String taskName = scanner.nextLine();
                    System.out.println("описание");
                    String taskDescription = scanner.nextLine();
                    Task task = new Task(taskName, taskDescription);
                    taskManager.createNewTask(task);
                    System.out.println(task);
                    break;
                case "2":
                    System.out.println("Введите название и описание");
                    String epicName = scanner.nextLine();
                    System.out.println("описание");
                    String epicDescription = scanner.nextLine();
                    taskManager.createNewEpic(new Epic(epicName, epicDescription));
                    break;
                case "3":
                    System.out.println("Введите название и описание");
                    String subTaskName = scanner.nextLine();
                    System.out.println("описание");
                    String subTaskDescription = scanner.nextLine();
                    System.out.println("№ эпика");
                    int epicId = scanner.nextInt();
                    taskManager.createNewSubTask(new SubTask(subTaskName, subTaskDescription), epicId);
                    break;
                case "4":
                    taskManager.deleteAllTasks();
                    break;
                case "5":
                    taskManager.deleteAllEpics();
                    break;
                case "6":
                    taskManager.deleteAllSubTasks();
                    break;
                case "7":
                    System.out.println("№ задачи");
                    int taskIdByDelete = scanner.nextInt();
                    taskManager.deleteTaskById(taskIdByDelete);
                    break;
                case "8":
                    System.out.println("№ эпика");
                    int epicIdByDelete = scanner.nextInt();
                    taskManager.deleteEpicById(epicIdByDelete);
                    break;
                case "9":
                    System.out.println("№ подзадачи");
                    int subTaskIdByDelete = scanner.nextInt();
                    taskManager.deleteSubTaskById(subTaskIdByDelete);
                    break;
                case "10":
                    System.out.println("Название");
                    String name = scanner.nextLine();
                    System.out.println("описание");
                    String description = scanner.nextLine();
                    System.out.println("статус");
                    String status = scanner.nextLine();
                    System.out.println("id");
                    int id = scanner.nextInt();
                    taskManager.updateTask(new Task(name, description, status), id);
                    break;
                case "11":
                    System.out.println("Название");
                    name = scanner.nextLine();
                    System.out.println("описание");
                    description = scanner.nextLine();
                    System.out.println("статус");
                    status = scanner.nextLine();
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.updateEpic(new Epic(name, description, status), id);
                    break;
                case"12":
                    System.out.println("название");
                    name = scanner.nextLine();
                    System.out.println("описание");
                    description = scanner.nextLine();
                    System.out.println("статус");
                    status = scanner.nextLine();
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.updateSubTask(new SubTask(name, description, status), id);
                    break;
                case "13":
                    taskManager.getListAllTasks();
                    break;
                case "14":
                    taskManager.getListAllEpics();
                    break;
                case "15":
                    taskManager.getListAllSubTasks();
                    break;
                case "16":
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.getTaskById(id);
                    break;
                case "17":
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.getEpicById(id);
                    break;
                case "18":
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.getSubTaskById(id);
                    break;
                case "19":
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.getSubTasksByEpicId(id);
                    break;
                default:
                    break;
            }
        }
    }

    public static void menu() {
        System.out.println("commands");
        System.out.println("1 - new task");
        System.out.println("2 - new epic");
        System.out.println("3 - new subTask");
        System.out.println("4 - delete all tasks");
        System.out.println("5 - delete all epics");
        System.out.println("6 - delete all subTasks");
        System.out.println("7 - delete task by id");
        System.out.println("8 - delete epic by id");
        System.out.println("9 - delete subTask by id");
        System.out.println("10 - update task");
        System.out.println("11 - update epic");
        System.out.println("12 - update subTask");
        System.out.println("13 - get list all tasks");
        System.out.println("14 - get list all epics");
        System.out.println("15 - get list all subTasks");
        System.out.println("16 - get task by id");
        System.out.println("17 - get epic by id");
        System.out.println("18 - get subTask by id");
        System.out.println("19 - get subTasks by epicId");
        System.out.println("0 - exit");
    }
}
