package com.design.template_method

import com.design.decorator.chapter07.println

abstract class CaffeineBeverage {

    // kotlin 은 기본적으로 final 이기 떄문에 붙일 필요는 없음
    final fun prepareRecipe() {
        boilWater()
        brew()
        pourInCup()
        addCondiments()
    }

    abstract fun brew()

    abstract fun addCondiments()

    fun boilWater() {
        println("물 끓이는 중")
    }

    fun pourInCup() {
        println("컵에 따라는 중")
    }
}


