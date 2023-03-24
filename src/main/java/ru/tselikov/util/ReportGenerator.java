package ru.tselikov.util;

import ru.tselikov.Entity.Subscriber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Класс генератор отчетов.
 */
public class ReportGenerator {
    private final DataManager dataManager; // Менеджер данных
    private final String directoryPath; // Стандартная директория хранения отчетов
    private final String reportPrefix; // Префикс, который будет в названии у каждого отчета
    private final String reportFormat; // Формат в котором мы сохраняем файл

    public ReportGenerator() {
        this.dataManager = new DataManager();
        this.directoryPath = "reports";
        this.reportPrefix = "report_";
        this.reportFormat = ".txt";
    }

    /**
     * Метод, который генерирует отчет.
     * @param sourcePath пусть к файлу, из которого сгенерировать отчет
     * @throws IOException когда файл не найден
     */
    public void generate(String sourcePath) throws IOException {
        this.dataManager.processData(sourcePath); // Обрабатываем данные

        BufferedWriter writer;

        createDirectory(directoryPath);

        // Перебираем всех абонентов и генерируем отчет
        dataManager.subscribers().values().forEach(subscriber -> {});
        for (Subscriber subscriber : dataManager.subscribers().values()) {
            writer = new BufferedWriter(new FileWriter(
                    String.format("%s/%s%d%s", directoryPath, reportPrefix, subscriber.phoneNumber(), reportFormat)));

            // Верх отчета с информацией об абоненте и шапкой таблицы
            StringBuilder report = new StringBuilder("Tariff index: ")
                    .append(subscriber.operations().last().tariff())
                    .append("\n-----------------------------------------------------------------------------")
                    .append("\nReport for phone number ").append(subscriber.phoneNumber()).append(":")
                    .append("\n-----------------------------------------------------------------------------")
                    .append("\n| Call Type |   Start Time        |     End Time        | Duration | Cost   |")
                    .append("\n-----------------------------------------------------------------------------");

            // Середина отчета с информацией по звонкам
            subscriber.operations().forEach(operation -> {
                        operation.calculateCost(); // Считаем стоимость
                        report.append("\n").append(operation);
                    }
            );

            String totalCost = new DecimalFormat("0.00").format(subscriber.calculateTotalCost()); // Считаем полную стоимость

            // Низ отчета с информацией об общей стоимости всех звонков за весь период
            report.append("\n-----------------------------------------------------------------------------")
                    .append(String.format("\n|                                           Total Cost: |     %s rubles  |", totalCost))
                    .append("\n-----------------------------------------------------------------------------");

            writer.write(report.toString());
            writer.close();
        }
        Logger.info("Отчеты сгенерированы!");
    }

    /**
     * Метод, который созадет директорию.
     * @param directoryPath путь к директории
     */
    private void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
