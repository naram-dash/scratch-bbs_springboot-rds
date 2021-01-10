package com.example.bbs_rds.user.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "bbs_user")
data class User(
    @Id
    val id: UUID,
    val username: String,
    val password: String
)