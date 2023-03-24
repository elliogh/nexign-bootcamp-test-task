package ru.tselikov.Entity;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс представления звонка.
 */
public class Operation {
    private final CallType callType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final long duration; // Продожительность звонка
    private double cost; // Цена звонка
    private final Tariff tariff;

    /**
     * Первичный конструктор.
     *
     * @param callType тип вызова
     * @param startTime время начала звонка
     * @param endTime время окончания звонка
     * @param tariff тариф
     */
    public Operation(CallType callType, LocalDateTime startTime, LocalDateTime endTime, Tariff tariff) {
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime).toSeconds();
        this.tariff = tariff;
        this.cost = 0; // Цена по умолчанию 0, потом поменяется
    }

    /**
     * Метод для подсчета стоимости звонка.
     */
    public void calculateCost() {
        double defaultMinutePrice = this.tariff.defaultMinutePrice();
        double tariffMinutePrice = this.tariff.tariffMinutePrice();
        boolean incomingFree = this.tariff.isIncomingFree();
        CallType callType = this.callType;
        long duration = this.duration / 60; // В минутах
        if (duration % 60 != 0) duration++; // Округляем в большую сторону

        //Проверка на тариф с бесплатными входящими и на входящий звонок
        if (incomingFree && callType.equals(CallType.INCOMING)) {
            this.cost = 0;
            return;
        }

        // Каждую минуту звонка проверяем закончились ли минуты в пакете
        while (duration != 0) {
            duration--;
            if (this.tariff.minutesLeft() != 0) {
                this.tariff.setMinutesLeft(this.tariff.minutesLeft() - 1); // Не закончились - вычитаем минуты из пакета
                this.cost += tariffMinutePrice;                            // и считаем по тарифу
            } else {
                this.cost += defaultMinutePrice; // Когда закончились, то считаем по обычному стандартному тарифу
            }
        }
    }

    /**
     * Метод, который возвращает форматированную строку с информацией об объекте.
     *
     * @return форматированная строка
     */
    @Override
    public String toString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = startTime.format(dateFormat);
        String endTimeStr = endTime.format(dateFormat);
        String durationStr = String.format("%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, duration % 60);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String costStr = decimalFormat.format(cost);
        return costStr.length() == 4?
                String.format("|     %s    | %s | %s | %s |  %s  |", callType.code(), startTimeStr, endTimeStr, durationStr, costStr)
                : String.format("|     %s    | %s | %s | %s |  %s |", callType.code(), startTimeStr, endTimeStr, durationStr, costStr);
    }

    public LocalDateTime startTime() {
        return startTime;
    }

    public LocalDateTime endTime() {
        return endTime;
    }

    public long duration() {
        return duration;
    }

    public double cost() {
        return cost;
    }

    public Tariff tariff() {
        return tariff;
    }
}
