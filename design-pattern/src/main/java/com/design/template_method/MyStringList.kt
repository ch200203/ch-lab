package com.design.template_method

class MyStringList(
    private val myList: Array<String>,
) : AbstractList<String>() {
    override fun get(index: Int): String {
        return myList[index]
    }

    override val size: Int
        get() = myList.size

    fun set(index: Int, item: String): String {
        val oldString = myList[index]
        myList[index] = item
        return oldString
    }
}
