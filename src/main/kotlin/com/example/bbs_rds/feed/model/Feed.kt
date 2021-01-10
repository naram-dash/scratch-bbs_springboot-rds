package com.example.bbs_rds.feed.model

import com.example.bbs_rds.user.model.User
import java.util.*
import javax.persistence.*

@Entity
data class Feed(
    @Id
    val id: UUID,
    val title: String,
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val writer: User,
)
