package ru.tselikov;

import ru.tselikov.util.Logger;
import ru.tselikov.util.ReportGenerator;

import java.io.IOException;

/**
 * Основной класс.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Logger.enableDebug(false); // Установить режим debug

        // Путь к CDR файлу
        String resourcePath = "resources/cdr.txt"; // ../resources/cdr.txt если запускаете jar из target

        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generate(resourcePath); // Запуск генератора отчетов
    }
}