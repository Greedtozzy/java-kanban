package test;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.util.Scanner;

public class Test {
    TaskManager taskManager = Managers.getDefault();
    TaskManager fileBackedTM = Managers.getFileBackedTaskManager();
    public void testSprint4() {
        Task task = new Task("taskName", "taskDescription");
        int task1 = taskManager.createNewTask(task);
        System.out.println(task);

        Epic epic = new Epic("epicName", "epicDescription");
        int epic1 = taskManager.createNewEpic(epic);
        System.out.println(epic);

        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic.getId());
        int subTask1 = taskManager.createNewSubTask(subTask);
        System.out.println(subTask);

        System.out.println(taskManager.getTaskById(task1));
        System.out.println(taskManager.getEpicById(epic1));
        System.out.println(taskManager.getSubTaskById(subTask1));
        System.out.println(taskManager.getHistory());
    }

    public void testSprint5() {
        int task1 = taskManager.createNewTask(new Task("taskName1", "taskDescription1"));
        int task2 = taskManager.createNewTask(new Task("taskName2", "taskDescription2"));
        int epic1 = taskManager.createNewEpic(new Epic("epicName1", "epicDescription1"));
        int subTask1 = taskManager.createNewSubTask(new SubTask("subTaskName1", "subTaskDescription1", epic1));
        int subTask2 = taskManager.createNewSubTask(new SubTask("subTaskName2", "subTaskDescription2", epic1));
        int subTask3 = taskManager.createNewSubTask(new SubTask("subTaskName3", "subTaskDescription3", epic1));
        int epic2 = taskManager.createNewEpic(new Epic("epicName2", "epicDescription2"));
        taskManager.getTaskById(task2);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(task2);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epic2);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epic1);
        System.out.println(taskManager.getHistory());
        taskManager.getSubTaskById(subTask2);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTaskById(task2);
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpicById(epic1);
        System.out.println(taskManager.getHistory());
    }

    public void testSprint6_1() {
        int task1 = fileBackedTM.createNewTask(new Task("taskName", "taskDescription"));
        int epic1 = fileBackedTM.createNewEpic(new Epic("epicName", "epicDescription"));
        int subtask1 = fileBackedTM.createNewSubTask(new SubTask("subTaskName", "subTaskDescription", epic1));
        int task2 = fileBackedTM.createNewTask(new Task("taskName2", "taskDescription2"));
        fileBackedTM.deleteTaskById(task1);
        fileBackedTM.getSubTaskById(subtask1);
        fileBackedTM.getTaskById(task2);
    }

    public void testSprint6_2() {
        TaskManager loadFromFileTM = Managers.loadFromFile(new File("resources/recordedTasks.csv"));
        System.out.println(loadFromFileTM.getHistory());
        System.out.println(loadFromFileTM.getSubTaskById(3));
        System.out.println(loadFromFileTM.getTaskById(4));
        int taskInNewManager = loadFromFileTM.createNewTask(new Task("taskName", "taskDescription"));
        System.out.println(loadFromFileTM.getHistory());
        loadFromFileTM.getTaskById(5);
        System.out.println(loadFromFileTM.getHistory());
        System.out.println(loadFromFileTM.getEpicById(2).subTasksIds);
    }

    public void testMenu(Scanner scanner) {
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
                    Epic epic = new Epic(epicName, epicDescription);
                    taskManager.createNewEpic(epic);
                    System.out.println(epic);
                    break;
                case "3":
                    System.out.println("Введите название и описание");
                    String subTaskName = scanner.nextLine();
                    System.out.println("описание");
                    String subTaskDescription = scanner.nextLine();
                    System.out.println("№ эпика");
                    int epicId = scanner.nextInt();
                    SubTask subTask = new SubTask(subTaskName, subTaskDescription, epicId);
                    taskManager.createNewSubTask(subTask);
                    System.out.println(subTask);
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
                    TaskStatus status = TaskStatus.valueOf(scanner.nextLine());
                    System.out.println("id");
                    int id = scanner.nextInt();
                    taskManager.updateTask(new Task(name, description, status, id));
                    break;
                case "11":
                    System.out.println("Название");
                    name = scanner.nextLine();
                    System.out.println("описание");
                    description = scanner.nextLine();
                    System.out.println("статус");
                    status = TaskStatus.valueOf(scanner.nextLine());
                    System.out.println("id");
                    id = scanner.nextInt();
                    taskManager.updateEpic(new Epic(name, description, status, id));
                    break;
                case"12":
                    System.out.println("Название");
                    name = scanner.nextLine();
                    System.out.println("описание");
                    description = scanner.nextLine();
                    System.out.println("статус");
                    status = TaskStatus.valueOf(scanner.nextLine());
                    System.out.println("id");
                    id = scanner.nextInt();
                    System.out.println("epicId");
                    epicId = scanner.nextInt();
                    taskManager.updateSubTask(new SubTask(name, description, status, id, epicId));
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
                    System.out.println(taskManager.getTaskById(id));
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
                case "20":
                    Managers.getDefaultHistory().getHistory();
                    System.out.println(Managers.getDefaultHistory().getHistory());
                case "0":
                    return;
                default:
                    break;
            }
        }
    }

    public void menu() {
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
        System.out.println("20 - get tasks call history");
        System.out.println("0 - exit");
    }
}
