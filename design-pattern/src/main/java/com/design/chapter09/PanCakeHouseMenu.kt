package com.design.chapter09

class PanCakeHouseMenu {
    var menuItems: MutableList<MenuItem> = mutableListOf()

    init {
        addItem(
            name = "팬케이크 세트",
            description = "스크램블에그와 토스트가 곁들여진 팬케이크",
            vegetarian = true,
            price = 2.99
        )

        addItem(
            name = "레귤러 팬케이크 세트",
            description = "달걀프라이와 소시지가 곁들여진 팬케이크",
            vegetarian = false,
            price = 2.99
        )
    }


    fun addItem(name: String, description: String, vegetarian: Boolean, price: Double) {
        val menuItem = MenuItem(name, description, vegetarian, price)
        menuItems.add(menuItem)
    }
}
