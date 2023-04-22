/* Доброго времени суток!
Сергей, этот спринт был еще более адовый, чем предыдущий)
Поправил очень много в коде. Много где добавил проверки на null.
Тестовые классы реализовал с точки зрения тестирования верной работы всего функционала.
Поправил комментарии по коду, добавил новые.
Вроде как получилось сделать факультативное задание. Кажется, что поиск пересечений работает за О(1).
Так-же сделал возможным создание задач не только со стартом кратным 15 минутам. Здесь спасибо наставнику за подсказку)
Надеюсь, что проверка не доставит много хлопот =)
 */

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
            System.out.println("6 - тест спринт 7");
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