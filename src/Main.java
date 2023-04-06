/* Доброго времени суток!
Замечания проработал.
Вынес в отдельный класс методы, которые отвечали за обработку задач и строк из файла.
Была мысль разобрать метод loadFromFile() на несколько методов поменьше, которые можно так-же вынести в отдельный класс
(пусть даже в тот же). Правильно ли было так сделать? Напугало количество обращений одного класса к другому и обратно.
Бадминтон получается...
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