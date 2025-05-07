package com.design.decorator.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeverageTest {

    @Test
    void espressoTest() {
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() + " $" + beverage.cost());
        assertEquals("에스프레소", beverage.getDescription());
        assertEquals(1.99, beverage.cost());
    }

    @Test
    void mochaTest() {
        Beverage beverage = new Espresso();
        beverage = new Mocha(beverage);
        beverage = new Mocha(beverage);

        assertEquals("에스프레소모카모카", beverage.getDescription());
        assertEquals(1.99 + .20 + .20, beverage.cost());
    }

}
