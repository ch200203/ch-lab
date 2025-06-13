package com.design.chapter09

class DinerMenuIterator(var items: Array<MenuItem>) : Iterator {
    var position: Int = 0

    override fun next(): Any {
        val menuItem = items[position]
        position++
        return menuItem
    }

    override fun hasNext(): Boolean {
        //식당은 메뉴 개수를 정해놓기 때문에 마지막에 도달했는지 뿐 아니라
        //현재 인덱스에 메뉴가 있는지도 확인을 해야함
        if (position >= items.size || items[position] == null) {
            return false
        } else {
            return true
        }
    }
}
