package com.design.template_method

class Tea : CaffeineBeverage() {

    override fun brew() {
        println("찻잎을 우려내는 중")
    }

    override fun addCondiments() {
        println("레몬을 추가하는 중")
    }
}
