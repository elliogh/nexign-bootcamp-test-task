package ru.tselikov.Entity;

/**
 * Класс для представления тарифа.
 */
public class Tariff {
    private final String code;
    private final String name;
    private final double price;
    private final double defaultMinutePrice;
    private final double tariffMinutePrice;
    private int minutesLeft;
    private final boolean incomingFree;

    /**
     * Первичный конструктор.
     *
     * @param code               код
     * @param name               название
     * @param price              цена
     * @param defaultMinutePrice стандартная стоимость звонка
     * @param tariffMinutePrice  стоимость звонка в пределах тарифа
     * @param minutesLeft        остаток минут
     * @param incomingFree       входящие бесплатны
     */
    public Tariff(String code, String name, double price, double defaultMinutePrice, double tariffMinutePrice, int minutesLeft, boolean incomingFree) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.defaultMinutePrice = defaultMinutePrice;
        this.tariffMinutePrice = tariffMinutePrice;
        this.minutesLeft = minutesLeft;
        this.incomingFree = incomingFree;
    }

    /**
     * Вторичный конструктор, который предназначен для создания копии.
     *
     * @param tariff тариф
     */
    public Tariff(Tariff tariff) {
        this(tariff.code(),
                tariff.name(),
                tariff.price(),
                tariff.defaultMinutePrice(),
                tariff.tariffMinutePrice(),
                tariff.minutesLeft(),
                tariff.isIncomingFree()
        );
    }

    @Override
    public String toString() {
        return code;
    }

    public String code() {
        return code;
    }

    public String name() {
        return name;
    }

    public double price() {
        return price;
    }

    public double defaultMinutePrice() {
        return defaultMinutePrice;
    }

    public double tariffMinutePrice() {
        return tariffMinutePrice;
    }

    public int minutesLeft() {
        return minutesLeft;
    }

    public boolean isIncomingFree() {
        return incomingFree;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
}
