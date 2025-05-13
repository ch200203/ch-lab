package com.design.decorator.chapter07;

import java.util.Random;

public class DuckAdapter implements Turkey {
    Duck duck;

    public DuckAdapter(Duck duck) {
        this.duck = duck;
    }

    @Override
    public void gobble() {
        duck.quack();
    }

    @Override
    public void fly() {
        if (new Random().nextInt(5) == 0) {
            duck.fly();
        }
    }
}
