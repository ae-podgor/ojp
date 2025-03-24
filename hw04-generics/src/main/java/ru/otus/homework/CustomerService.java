package ru.otus.homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customerMap = new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallest = customerMap.firstEntry();
        if (smallest == null) {
            return null;
        }
        Customer key = smallest.getKey();
        Customer copy = new Customer(key.getId(), key.getName(), key.getScores());
        return Map.entry(copy, smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> highered = customerMap.higherEntry(customer);
        if (highered == null) {
            return null;
        }
        Customer key = highered.getKey();
        Customer copy = new Customer(key.getId(), key.getName(), key.getScores());
        return Map.entry(copy, highered.getValue());
    }

    public void add(Customer customer, String data) {
        Customer copy = new Customer(customer.getId(), customer.getName(), customer.getScores());
        customerMap.put(copy, data);
    }
}
