package com.exposed.board.service

import com.exposed.board.domain.User
import com.exposed.board.domain.UserEntity
import com.exposed.board.domain.UserEntity.email
import com.exposed.board.domain.UserId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService {
    fun findByUserId(id: UserId): User? {
        // dsl transactional 없이도 가능함
        return UserEntity.selectAll().where { UserEntity.id eq id.value }.firstOrNull()?.let {
            User(
                id = UserId(it[UserEntity.id].value),
                name = it[UserEntity.name],
                email = it[UserEntity.email],
                password = it[UserEntity.password],
            )
        }
    }

    fun create(request: UserCreateRequest): UserId {
        val id = UserEntity.insertAndGetId {
            it[name] = request.name
            it[email] = request.email
            it[password] = request.password
        }
        return UserId(id.value)
    }

    data class UserCreateRequest(val name: String, val email: String, val password: String)


}
