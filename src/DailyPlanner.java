import tasks.*;
import utility.ValidateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;

public class DailyPlanner {
    private static final Map<Integer, Task> tasks = new HashMap<>();
    private static final Map<Integer, Task> tasksArchive = new TreeMap<>();

    public static void inputTask(Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.print("Введите название задачи: ");
            String heading = ValidateUtil.checkLine(scanner.nextLine());
            System.out.println("Введите описание задачи: ");
            String description = ValidateUtil.checkLine(scanner.nextLine());
            System.out.println("""
                    Введите тип задачи:\s
                    0 - Задача личная\s
                    1 - Задача рабочая""");
            boolean isWork = createIsWork(scanner.nextInt());
            System.out.println("Вы ввели: " + getStringIsWork(isWork) + "\n");
            System.out.println("""
                    Укажите повторяемость задачи:\s
                    0 - Однократная\s
                    1 - Ежедневная\s
                    2 - Еженедельная\s
                    3 - Ежемесячная\s
                    4 - Ежегодная
                    """);
            int repeatable = scanner.nextInt();
            System.out.println("Вы ввели: " + createRepeatable(repeatable));
            System.out.println();
            System.out.println("Введите дату и время постановки задачи (dd.MM.yyyy HH:mm): ");
            scanner.nextLine();
            try {
                LocalDateTime localDateTime = createDateAndTime(scanner.nextLine());
                System.out.println("Вы ввели: " + localDateTime + "\n");
                Task task = createTask(heading, description, isWork, localDateTime, repeatable);
                addTask(task);
                System.out.println("Задача создана! \n" +
                        "Проверим:");
                System.out.println(task.toString());
            } catch (DateTimeParseException e) {
                System.out.println("Формат ввода должен соответствовать шаблону dd.MM.yyyy HH:mm, попробуйте еще раз.");
            }
        System.out.println("\n" +
                "Для выхода в главное меня нажмите Enter");
        scanner.nextLine();
        } catch (RuntimeException e) {
            System.out.println("Информация введена некорректно!\nПопробуйте ещё раз.\n");
        }
    }

    private static boolean createIsWork(int isWork) {
        if (isWork == 0) {
            return false;
        }
        if (isWork == 1) {
            return true;
        }
            throw new InputMismatchException("Введен неправильный тип задачи");
        }

    private static String createRepeatable(int number) {
        String repeatability;
        if (number == 0) {
            repeatability = "Однократная";
        } else if (number == 1) {
            repeatability = "Ежедневная";
        } else if (number == 2) {
            repeatability = "Еженедельная";
        } else if (number == 3) {
            repeatability = "Ежемесячная";
        } else if (number == 4) {
            repeatability = "Ежегодная";
        } else {
            throw new InputMismatchException("Введена неправильно повторяемость задачи");
        }
        return repeatability;
    }

    private static LocalDateTime createDateAndTime(String dateAndTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.parse(dateAndTime, formatter);
    }

    private static String getStringIsWork(boolean isWork) {
        if (isWork) {
            return "Задача рабочая";
        } else {
            return "Задача личная";
        }
    }

    private static Task createTask(String heading, String description, boolean isWorkTask, LocalDateTime localDateTime, int repeatable) {
        return switch (repeatable) {
            case 0 -> new OneTimeTask(heading, description, isWorkTask, localDateTime);
            case 1 -> new DailyTask(heading, description, isWorkTask, localDateTime);
            case 2 -> new WeeklyTask(heading, description, isWorkTask, localDateTime);
            case 3 -> new MonthlyTask(heading, description, isWorkTask, localDateTime);
            case 4 -> new AnnualTask(heading, description, isWorkTask, localDateTime);
            default -> null;
        };
    }

    private static void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public static void getTasksForOneDay(Scanner scanner) {
        System.out.println("Введите дату в формате dd.MM.yyyy:");
        try {
            String date = scanner.next();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            TreeMap<LocalTime, Task> tasksOfTheDay = getTasksForDay(localDate);
            System.out.println("Список задач на " + localDate + "(" + getDayOfWeek(localDate) + "):");
            if (!tasksOfTheDay.isEmpty()) {
                for (Task task : tasksOfTheDay.values()) {
                    System.out.println(task);
                }
            } else {
                System.out.println("< Задач нет >");
            }
        } catch (RuntimeException e) {
            System.out.println("Формат ввода должен соответствовать шаблону dd.MM.yyyy, попробуйте еще раз.");
        }
        scanner.nextLine();
        System.out.println("\nДля выхода в главное меня нажмите Enter\n");
        scanner.nextLine();
    }

    private static TreeMap<LocalTime, Task> getTasksForDay(LocalDate date) {
        TreeMap<LocalTime, Task> tasksOfTheDay = new TreeMap<>();
        LocalDateTime startRequiredDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endRequiredDay = LocalDateTime.of(date, LocalTime.MAX);
        for (Task task : tasks.values()) {
            if (task instanceof Repeatable) {
                LocalDateTime taskDate = ((Repeatable) task).getNextDateAndTime(startRequiredDay);
                if (taskDate.isBefore(endRequiredDay)) {
                    tasksOfTheDay.put(task.getLocalDateTime().toLocalTime(), task);
                }
            } else {
                LocalDateTime taskDate = task.getLocalDateTime();
                if (taskDate.toLocalDate().isEqual(date)) {
                    tasksOfTheDay.put(task.getLocalDateTime().toLocalTime(), task);
                }
            }
        }
        return tasksOfTheDay;
    }

    private static String getDayOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Locale localeRu = new Locale("ru", "RU");
        return dayOfWeek.getDisplayName(TextStyle.FULL, localeRu);
    }

    public static void deleteTask(Scanner scanner) {
        System.out.println("Введите ID задачи, которую требуется удалить.\n" +
                "Ниже представлен список задач:");
        getAllActiveTasks();
        int id = scanner.nextInt();
        if (tasks.containsKey(id)) {
            tasks.get(id).setRemoteTask(false);
            tasksArchive.put(tasks.get(id).getId(), tasks.get(id));
            System.out.println("Задача под номер " + id + " удалена.\n");
        } else {
            System.out.println("Не могу найти ID запрашиваемой задачи.\n");
        }
    }

    public static void getDeletedTasks() {
        System.out.println("Список удаленных задач:");
        if (!tasksArchive.isEmpty()) {
            for (Map.Entry<Integer, Task> entry : tasksArchive.entrySet()) {
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println("< Удаленные задачи отсутствуют >\n");
        }
    }

    public static void editTask(Scanner scanner) {
        try {
            System.out.println("""
                Введите ID задачи, которую Вы хотите отредактировать.
                Ниже представлен перечень всех задач:
                """);
        getAllActiveTasks();
        int taskID = scanner.nextInt();
        checkTaskId(taskID);
        System.out.println("Данная задача имеется в списке.");
        System.out.println("""
                Введите номер пункта, который требуется изменить:
                0. Название;
                1. Описание;
                2. Тип задачи;
                3. Дата и время постановки задачи;
                """);
        int numberToChange = scanner.nextInt();
        switch (numberToChange) {
            case 0 -> {
                scanner.nextLine();
                System.out.println("Введите новое название задачи: ");
                String taskName = scanner.nextLine();
                changeTaskName(taskID, taskName);
                System.out.println("Сохранено!\n" +
                        "Новое название данной задачи: " +
                        tasks.get(taskID).getHeading() + "\n");
            }
            case 1 -> {
                scanner.nextLine();
                System.out.println("Введите новое описание задачи: ");
                String taskDescription = scanner.nextLine();
                changeTaskDescription(taskID, taskDescription);
                System.out.println("Сохранено!\n" +
                        "Новое описание данной задачи: " +
                        tasks.get(taskID).getDescription() + "\n");
            }
            case 2 -> {
                scanner.nextLine();
                System.out.println("""
                        Введите новый тип задачи:\s
                        0. Личная задача
                        1. Рабочая задача
                        """);
                boolean isWork = createIsWork(scanner.nextInt());
                changeIsWork(taskID, isWork);
            }
            case 3 -> {
                scanner.nextLine();
                System.out.println("Введите новые дату и время постановки задачи (формат ввода: dd.MM.yyyy HH:mm): ");
                LocalDateTime dateAndTimeTask = createDateAndTime(scanner.nextLine());
                changeTimeAndDateTask(taskID, dateAndTimeTask);
                System.out.println("Сохранено!\n" +
                        "Новые дата и время постановки задачи: " +
                        tasks.get(taskID).getLocalDateTime() + "\n");
            }
        }
        } catch (RuntimeException e) {
            System.out.println("Что-то пошло не так. Попробуйте ещё раз...\n");
        }
    }

    private static void getAllActiveTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    private static void checkTaskId(int id) {
        if (!tasks.containsKey(id)) {
            throw new IllegalArgumentException("Задачи с таким ID не найдено");
        }
    }

    private static void changeTaskName(int id, String taskName) {
        tasks.get(id).setHeading(taskName);
    }

    private static void changeTaskDescription(int id, String taskDescription) {
        tasks.get(id).setDescription(taskDescription);
    }

    private static void changeTimeAndDateTask(int id, LocalDateTime dateTime) {
        tasks.get(id).setLocalDateTime(dateTime);
    }

    private static void changeIsWork(int id, boolean isWork) {
        tasks.get(id).setIsWorkTask(isWork);
            System.out.println("Сохранено!\n" +
                    "Вы изменили тип задачи на: " + getStringIsWork(isWork) + "\n");
        }

    public static void sortByDate(Scanner scanner) {
        Map<LocalDateTime, Task> sortedTask = new TreeMap<>();
        System.out.println("Cписок задач сгруппированных по датам: ");
        if ((!tasks.isEmpty())) {
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                sortedTask.put(entry.getValue().getLocalDateTime(), entry.getValue());
                }
        } else {
            System.out.println("< Задач нет >\n");
        }
        for (Map.Entry<LocalDateTime, Task> taskEntry : sortedTask.entrySet()) {
            System.out.println(taskEntry.getValue());
        }
        scanner.nextLine();
        System.out.println("\n" +
                "Для выхода в главное меня нажмите Enter");
        scanner.nextLine();
    }
}