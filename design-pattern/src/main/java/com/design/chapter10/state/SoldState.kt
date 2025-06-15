package com.design.chapter10.state

import com.design.chapter10.GumballMachine

class SoldState(private val machine: GumballMachine) : State {
    override fun insertQuarter() {
        TODO("Not yet implemented")
    }

    override fun ejectQuarter() {
        TODO("Not yet implemented")
    }

    override fun turnCrank() {
        TODO("Not yet implemented")
    }

    override fun dispense() {
        machine.releaseBall()

        if (machine.count > 0) {
            machine.setState(machine.getNoQuarterState())
        } else {
            println("Oops, out of gumballs!")
            machine.setState(machine.getSoldOutState())
        }
    }
}
