package com.study;

import java.util.ArrayList;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        // 공변성
        Object[] Covariance = new Integer[10];

        // 반공변성
        Integer[] Contravariance = (Integer[]) Covariance;


        Object parent = new Object();
        Integer child = new Integer(1);

        parent = child; // 다형성 (업캐스팅)

        Object parent = new Integer(1);
        Integer child2 = (Integer) parent; // 다형성 (다운캐스팅)
    }

    private void vv() {
        Collection<Integer> parent = new ArrayList<>();
        ArrayList<Integer> child = new ArrayList<>();

        parent = child; // 다형성 (업캐스팅)
    }

    private void vv2() {
        ArrayList<Object> parent = new ArrayList<>();
        ArrayList<Integer> child = new ArrayList<>();

        parent = child; // ! 업캐스팅 불가능
        child = parent; // ! 다운캐스팅 불가능
    }
}