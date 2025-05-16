package com.design.decorator.chapter07;

public class DuckTestDrive {
    public static void main(String[] args) {
        Duck duck = new MallardDuck();

        Turkey turkey = new WildTurkey();
        Duck turkeyAdapter = new TurkeyAdapter(turkey); // Turkey 객체를 어댑터로 감싸서 Duck 처럼 보이게함

        System.out.println("칠면조가 말하길");
        turkey.gobble();
        turkey.fly();

        System.out.println("오리가 말하길");
        testDuck(duck);

        System.out.println("칠면조 어댑터가 말하기를");
        testDuck(turkeyAdapter);

    }

    static void testDuck(Duck duck) {
        duck.quack();
        duck.fly();
    }
}
