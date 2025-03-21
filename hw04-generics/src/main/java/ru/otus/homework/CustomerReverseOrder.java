package ru.otus.homework;

import java.util.ArrayDeque;

public class CustomerReverseOrder {

    private final ArrayDeque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        return customers.pollLast();
    }
}
