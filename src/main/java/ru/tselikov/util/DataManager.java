package ru.tselikov.util;

import ru.tselikov.Entity.CallType;
import ru.tselikov.Entity.Operation;
import ru.tselikov.Entity.Subscriber;
import ru.tselikov.Entity.Tariff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс менеджер данных.
 */
public class DataManager {
    private final Map<Long, Subscriber> subscribers; // Все абоненты
    private final Map<String, Tariff> possibleTariffs; // Все возможные тарифы
//    private final static Logger logger = new Logger();

    public DataManager() {
        this.subscribers = new HashMap<>();
        this.possibleTariffs = Map.of(
                "06", new Tariff("06", "Безлимит", 100, 1d, 0, 300, false),
                "03", new Tariff("03", "Поминутный", 0, 1.5, 0, 0, false),
                "11", new Tariff("11", "Обычный", 0, 1.5, 0.5, 100, true)
        );
    }

    /**
     * Метод для обработки CDR файла.
     *
     * @param sourcePath путь к файлу
     * @throws IOException когда файл не найден
     */
    public void processData(String sourcePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
            String line;

            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
            reader.close();
            Logger.info("Файл прочитан!");
        } catch (FileNotFoundException e) {
            Logger.error("Файл не найден");
            e.printStackTrace();
        } catch (IOException e) {
            Logger.error("Ошибка во время чтения файла");
            e.printStackTrace();
        }

    }

    /**
     * Метод для парсинга строки в оъект Operation.
     *
     * @param line строка
     * @see Operation
     */
    private void parseLine(String line) {
        try {
            // Достаем из строки все необходимые данные
            String[] arguments = line.split(",");

            long phoneNumber = Long.parseLong(arguments[1].trim());
            CallType callType = arguments[0].trim().equals(CallType.OUTGOING.code()) ? CallType.OUTGOING : CallType.INCOMING;

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime startTime = LocalDateTime.parse(arguments[2].trim(), dateFormat);
            LocalDateTime endTime = LocalDateTime.parse(arguments[3].trim(), dateFormat);

            String tariffType = arguments[4].trim();
            Tariff tariff = new Tariff(possibleTariffs.get(tariffType));

            if (!this.subscribers.containsKey(phoneNumber)) {
                this.subscribers.put(phoneNumber, new Subscriber(phoneNumber)); // Если абонент с таким телефоном не существует, то создаем его
            } else {
                tariff = this.subscribers.get(phoneNumber).operations().last().tariff(); // Берем последний тариф
            }

            this.subscribers.get(phoneNumber).operations().add(new Operation(
                    callType,
                    startTime,
                    endTime,
                    tariff
            ));

            Logger.debug(
                    this.subscribers.get(phoneNumber) + ": " +
                    this.subscribers.get(phoneNumber).operations().last().toString().replace("  0,00  |", "")
            );
        } catch (NumberFormatException e) {
            Logger.error("Некорректные данные в файле. Проверьте номера телефонов");
            e.printStackTrace();
        } catch (DateTimeException e) {
            Logger.error("Некоректная дата в файле. Должна быть yyyyMMddHHmmss");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            Logger.error("Неверное количество аргументов в файле. Должно быть 5");
            e.printStackTrace();
        }
    }

    public Map<Long, Subscriber> subscribers() {
        return subscribers;
    }
}
