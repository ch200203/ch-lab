package com.exposed.board.domain

data class User(
    val id: UserId,
    val name: String,
    val email: String,
    val password: String,
)

@JvmInline
value class UserId(val value: Long)
