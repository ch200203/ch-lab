package com.design.decorator.chapter07;

public interface Turkey {
    public void gobble(); // 골골거리리는 소리를 냄
    public void fly(); // 날 수 있기ㄴ느함
}

class WildTurkey implements Turkey {
    @Override
    public void gobble() {
        System.out.println("골골");
    }

    @Override
    public void fly() {
        System.out.println("터키날다");
    }
}
