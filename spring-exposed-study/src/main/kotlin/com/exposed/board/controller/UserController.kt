package com.exposed.board.controller

import com.exposed.board.domain.UserId
import com.exposed.board.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.findByUserId(UserId(id))
        return if (user != null) {
            ResponseEntity.ok(
                UserResponse(
                    id = user.id.value,
                    name = user.name,
                    email = user.email,
                )
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

    data class UserResponse(
        val id: Long,
        val name: String,
        val email: String,
    )

    @PostMapping
    fun create(@RequestBody form: UserCreateRequestForm): ResponseEntity<UserCreateResponse> {
        val userId = userService.create(
            UserService.UserCreateRequest(
                name = form.name,
                email = form.email,
                password = form.password,
            )
        )

        return ResponseEntity.ok(
            UserCreateResponse(
                id = userId.value,
            )
        )

    }

    data class UserCreateRequestForm(
        val name: String,
        val email: String,
        val password: String,
    )

    data class UserCreateResponse(val id: Long)

}
