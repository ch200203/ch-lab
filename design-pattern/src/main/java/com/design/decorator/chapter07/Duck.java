package com.design.decorator.chapter07;

public interface Duck {
    public void quack();

    public void fly();
}

class MallardDuck implements Duck {
    @Override
    public void quack() {
        System.out.println("꽥!");
    }

    @Override
    public void fly() {
        System.out.println("오리 날다!");
    }
}

