package com.design.chapter10.state

import com.design.chapter10.GumballMachine

class HasQuarterState(private val machine: GumballMachine) : State {
    override fun insertQuarter() = println("이미 동전이 들어있습니다.")
    override fun ejectQuarter() {
        println("동전을 반환합니다.")
        machine.setState(machine.getNoQuarterState())
    }

    override fun turnCrank() {
        println("손잡이를 돌리셨습니다...")
        machine.setState(machine.getSoldState())
    }

    override fun dispense() = println("아직 상품이 나갈 준비가 안 됐습니다.")
}
