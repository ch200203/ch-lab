package com.design.chapter09

class DinnerMenu {
    companion object {
        const val MAX_ITEMS = 6
    }

    private val menuItems: Array<MenuItem?> = arrayOfNulls(MAX_ITEMS)
    var numberOfItems: Int = 0
        private set

    init {
        addItem("디너 스테이크", "두툼한 스테이크와 구운 채소", false, 15.99)
        addItem("채식주의자용 BLT", "통밀위에 콩고기 베이컨, 상추, 모마토를 얹은 메뉴", true, 2.99)
    }

    fun addItem(name: String, description: String, vegetarian: Boolean, price: Double) {
        if (numberOfItems >= MAX_ITEMS) {
            println("더 이상 메뉴를 추가할 수 없습니다. (최대 \$MAX_ITEMS개)")
            return
        }
        menuItems[numberOfItems] = MenuItem(name, description, vegetarian, price)
        numberOfItems++
    }

    fun getMenuItems(): List<MenuItem> =
        menuItems.filterNotNull()
}
