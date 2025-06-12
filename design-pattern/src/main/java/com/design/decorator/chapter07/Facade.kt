package com.design.decorator.chapter07

// 1) 서브시스템 클래스들
class Amplifier {
    fun on() = println("Amplifier on")
    fun off() = println("Amplifier off")
    fun setVolume(v: Int) = println("Set volume to $v")
}

class Tuner {
    fun on() = println("Tuner on")
    fun off() = println("Tuner off")
    fun setFrequency(freq: Double) = println("Tuner frequency set to $freq MHz")
}

class DvdPlayer {
    fun on() = println("DVD Player on")
    fun off() = println("DVD Player off")
    fun play(movie: String) = println("Playing movie: $movie")
    fun stop() = println("Stopping movie")
}

class Projector {
    fun on() = println("Projector on")
    fun off() = println("Projector off")
    fun wideScreenMode() = println("Projector in widescreen mode (16:9 aspect ratio)")
}

// 2) 퍼사드 클래스
class HomeTheaterFacade(
    private val amp: Amplifier,
    private val tuner: Tuner,
    private val dvd: DvdPlayer,
    private val projector: Projector
) {
    fun watchMovie(movie: String) {
        println("Get ready to watch a movie...")
        amp.on()
        amp.setVolume(5)
        projector.on()
        projector.wideScreenMode()
        dvd.on()
        dvd.play(movie)
    }

    fun endMovie() {
        println("Shutting movie theater down...")
        dvd.stop()
        dvd.off()
        projector.off()
        amp.off()
    }
}

// 3) 사용 예
fun main() {
    val amp = Amplifier()
    val tuner = Tuner()
    val dvd = DvdPlayer()
    val projector = Projector()
    val homeTheater = HomeTheaterFacade(amp, tuner, dvd, projector)

    homeTheater.watchMovie("Inception")
    println()
    homeTheater.endMovie()
}

fun println(str: String = "") {
    println(str)
}


