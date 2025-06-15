package com.design.chapter10

import com.design.chapter10.state.HasQuarterState
import com.design.chapter10.state.NoQuarterState
import com.design.chapter10.state.SoldOutState
import com.design.chapter10.state.SoldState
import com.design.chapter10.state.State

class GumballMachine(val numberGumballs: Int) {

    // 모든 상태객체 선언
    private val soldOutState = SoldOutState()
    private val noQuarterState = NoQuarterState(this)
    private val hasQuarterState = HasQuarterState()
    private val soldState = SoldState(this)

    // 상태 인스턴스 변수 선언(State)
    private var state: State = if (numberGumballs > 0) noQuarterState else soldOutState

    // count 인스턴스 변수에는 알맹이 갯수 저장
    var count: Int = numberGumballs
        private set


    fun insertQuarter() = state.insertQuarter()
    fun ejectQuarter() = state.ejectQuarter()
    fun turnCrank() {
        state.turnCrank()
        state.dispense()
    }

    fun setState(newState: State) {
        state = newState
    }

    fun releaseBall() {
        println("알맹이를 내보내고 있습니다.")
        if (count > 0) {
            count = count - 1
        }

    }


    // 상태 객체 접근용 (내부에서만 사용)
    fun getSoldOutState()   = soldOutState
    fun getNoQuarterState() = noQuarterState
    fun getHasQuarterState()= hasQuarterState
    fun getSoldState()      = soldState

}
