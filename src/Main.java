/* Доброго времени суток!

Спасибо за разьяснения. Разобрался.
Очень полезно было увидеть в живую, что можно работать с методом remove, который возвращает удаляемое V.
Ранее об этом не задумывался. Красиво и экономно по строкам получается.

Изменил методы по созданию задач. Теперь они возвращают id задачи.
Сразу вопрос. При создании subTask происходит проверка на существование эпика, к которому пытаемся подзадачу привязать.
При отсутствии эпика правильно ли будет вернуть 0? Возврат такого значения не должен ни на что влиять.

Исправил в тестере вызов по id. Теперь все работает корректно, не зависимо от очередности вызова тестов и их кол-ва =)
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
                case "0" :
                    return;
                default:
                    break;
            }
        }
    }
}
