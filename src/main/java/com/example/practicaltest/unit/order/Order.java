package com.example.practicaltest.unit.order;

import java.time.LocalDateTime;
import java.util.List;

import com.example.practicaltest.unit.beverage.Beverage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;
}
