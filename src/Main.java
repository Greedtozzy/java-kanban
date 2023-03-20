/* Доброго времени суток!

При вызове истории, задачи в ней сортируются по id. Не понимаю, почему так происходит.
Прошу подсказку.

Переделал main. в нем теперь только вызов тестера.
В тестер вынес всю старую менюшку с реализацией,
а так-же написал отдельный тест по 5 спринту(комманда 3 сразу после запуска).
 */

import Test.Test;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Test test = new Test();

    public static void main(String[] args) {
        while (true) {
            System.out.println("1 - тест через меню");
            System.out.println("2 - тест спринт 4");
            System.out.println("3 - тест спринт 5");
            System.out.println("0 - остановить программу");
            String command = scanner.nextLine();
            switch (command) {
                case "1" :
                    test.testMenu();
                    break;
                case "2" :
                    test.testSprint4();
                    break;
                case "3" :
                    test.testSprint5();
                    break;
                case "0" :
                    return;
                default:
                    break;
            }
        }
    }
}
