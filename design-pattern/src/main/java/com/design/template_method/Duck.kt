package com.design.template_method

import java.util.Arrays

class Duck(
    private val name: String,
    private val weight: Int,
) : Comparable<Duck> {


    override fun compareTo(other: Duck): Int {
        if (this.weight < other.weight) {
            return -1
        } else if (this.weight == other.weight) {
            return 0
        } else { // this.weight > other.weight
            return 1;
        }
    }

    override fun toString(): String {
        return "Duck(name='$name', weight=$weight)"
    }

}

fun main() {
    val ducks = arrayOf(
        Duck("오리1", 8),
        Duck("오리2", 2),
        Duck("오리3", 5),
        Duck("오리4", 4),
        Duck("오리5", 7),
    )

    println("정렬 전 : ")
    display(ducks)

    Arrays.sort(ducks)
    println("정렬 후 : ")
    display(ducks)
}

fun display(ducks: Array<Duck>) {
    for (duck in ducks) {
        println(duck)
    }
}
