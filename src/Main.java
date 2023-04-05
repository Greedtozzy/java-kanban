/* Доброго времени суток!
Сложное ТЗ...

Создал класс, который наследуется от InMemoryHistoryManager.
В нем реальизовал запись в .csv файл во всех методах, в которых это показалось уместно.
В тестер добавил 2 метода. 6_1 отвечает за первоначальный запуск менеджера. Он записывает в файл все действия.
6_2 разворачивает новый экземпляр менеджера из файла.
После перезапуска приложения все работает.
Очень вероятно наплел бред с исключениями.

Прошу обратить внимание на развертку в Managers. Помимо своего основного функционала, в методе перезаписывается id,
что позволяет дальше создавать таски без ошибок с дублированием id.
 */

import test.Test;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Test test = new Test();
        while (true) {
            System.out.println("1 - тест через меню");
            System.out.println("2 - тест спринт 4");
            System.out.println("3 - тест спринт 5");
            System.out.println("4 - тест спринт 6_1");
            System.out.println("5 - тест спринт 6_2");
            System.out.println("0 - остановить программу");
            String command = scanner.nextLine();
            switch (command) {
                case "1" :
                    test.testMenu(scanner);
                    break;
                case "2" :
                    test.testSprint4();
                    break;
                case "3" :
                    test.testSprint5();
                    break;
                case "4" :
                    test.testSprint6_1();
                    break;
                case "5" :
                    test.testSprint6_2();
                    break;
                case "0" :
                    return;
                default:
                    break;
            }
        }
    }
}