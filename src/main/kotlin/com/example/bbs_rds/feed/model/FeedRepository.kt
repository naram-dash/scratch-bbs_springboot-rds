package com.example.bbs_rds.feed.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

interface FeedRepository: JpaRepository<Feed, UUID>, QuerydslPredicateExecutor<Feed> {
    override fun findById(id: UUID): Optional<Feed>
}