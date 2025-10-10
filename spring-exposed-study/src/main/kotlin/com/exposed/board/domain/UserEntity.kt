package com.exposed.board.domain

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable


object UserEntity : LongIdTable() {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
}
