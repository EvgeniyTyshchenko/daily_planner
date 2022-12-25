import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                System.out.println("Выберите пункт меню:");
                printMenu();
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            DailyPlanner.inputTask(scanner);
                            break;
                        case 2:
                            DailyPlanner.editTask(scanner);
                            break;
                        case 3:
                            DailyPlanner.deleteTask(scanner);
                            break;
                        case 4:
                            DailyPlanner.getTasksForOneDay(scanner);
                            break;
                        case 5:
                            DailyPlanner.getDeletedTasks();
                            break;
                        case 6:
                            DailyPlanner.sortByDate(scanner);
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("""
                1. Добавить задачу\s
                2. Редактировать задачу\s
                3. Удалить задачу\s
                4. Получить задачи на указанный день\s
                5. Получить все удаленные задачи\s   
                6. Получить список задач сгруппированных по датам\s            
                0. Выход"""
        );
    }
}