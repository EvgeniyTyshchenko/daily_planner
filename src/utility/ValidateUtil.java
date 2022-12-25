package utility;

import java.util.InputMismatchException;

public class ValidateUtil {
    public static String validateString(String needConfirmation) {
        if (needConfirmation == null || needConfirmation.isEmpty() || needConfirmation.isBlank()) {
            throw new InputMismatchException("< Неправильный ввод данных >");
        } else {
            return needConfirmation;
        }
    }

    public static String checkLine(String needConfirmation) {
        if (needConfirmation.isEmpty()) {
            throw new InputMismatchException("Отсутствует информация");
        } else {
            return needConfirmation;
        }
    }
}