package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.service.DiscountService;

/**
 * Пример использования данных общего количества заказов
 */
@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/{userId}")
    public DiscountResponse getDiscount(@PathVariable("userId") String userId) {
        long totalOrders = discountService.getTotalOrdersForUser(userId);
        long percent = discountService.getDiscountPercentByOrders(totalOrders);
        return new DiscountResponse(userId, totalOrders, percent);
    }

    public record DiscountResponse(String userId, long totalOrders, long discountPercent) {}
}
