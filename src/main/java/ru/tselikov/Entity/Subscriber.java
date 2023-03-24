package ru.tselikov.Entity;

import java.util.TreeSet;

/**
 * Класс для представления абонента.
 */
public class Subscriber {
    private final long phoneNumber; // Номер телефона абонента
    private final TreeSet<Operation> operations; // Множество всех операций

    public Subscriber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.operations = new TreeSet<>((o1, o2) -> o1.startTime().compareTo(o2.endTime())); // Comparator по времени
    }

    /**
     * Метод для определения общей стоимости за весь период.
     */
    public double calculateTotalCost() {
        return operations.stream().mapToDouble(Operation::cost).sum()
                + operations.last().tariff().price();
    }

    public long phoneNumber() {
        return phoneNumber;
    }

    public TreeSet<Operation> operations() {
        return operations;
    }

    @Override
    public String toString() {
        return String.valueOf(phoneNumber);
    }
}
