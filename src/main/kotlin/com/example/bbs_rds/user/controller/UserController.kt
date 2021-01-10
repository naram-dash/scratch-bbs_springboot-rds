package com.example.bbs_rds.user.controller

import com.example.bbs_rds.user.model.User
import com.example.bbs_rds.user.model.UserRepository
import com.querydsl.core.types.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    val userRepository: UserRepository
) {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): User {
        userRepository.findById(id).let {
            if (it.isPresent) return it.get() else throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "user is not exist"
            )
        }
    }

    @GetMapping("")
    fun getUsers(
        @QuerydslPredicate(root = User::class) predicate: Predicate,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<User> {
        return userRepository.findAll(pageable)
    }

    data class PostUserReq(
        val username: String,
        val password: String
    )

    @PostMapping("")
    fun postUser(@RequestBody postUserReq: PostUserReq): User {
        val user = User(UUID.randomUUID(), postUserReq.username, postUserReq.password)
        return userRepository.save(user)
    }

    data class PutUserReq(
        val username: String,
        val password: String
    )

    @PutMapping("/{id}")
    fun putUser(@PathVariable id: UUID, @RequestBody putUserReq: PutUserReq): User {
        userRepository.findById(id).let {
            if (it.isPresent) {
                val user = it.get()
                return userRepository.save(user.copy(username = putUserReq.username, password = putUserReq.password))
            } else
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "user is not exist")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        userRepository.findById(id).let {
            if (it.isPresent) it.get() else throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "user is not exist"
            )
        }
    }
}