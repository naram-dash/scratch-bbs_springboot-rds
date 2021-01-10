package com.example.bbs_rds.user.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

interface UserRepository: JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {
    override fun findById(uuid: UUID): Optional<User>
}