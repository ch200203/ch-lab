package com.design.decorator.example;

public abstract class CondimentDecorator extends Beverage{ // Beverage 클래스 확장
    Beverage beverage; // 각 데코레이터가 감쌀 객체를 지정

    public abstract String getDescription();
}
