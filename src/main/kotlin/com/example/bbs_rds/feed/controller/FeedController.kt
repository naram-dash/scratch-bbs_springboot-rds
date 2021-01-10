package com.example.bbs_rds.feed.controller

import com.example.bbs_rds.feed.model.Feed
import com.example.bbs_rds.feed.model.FeedRepository
import com.example.bbs_rds.user.model.User
import com.example.bbs_rds.user.model.UserRepository
import com.querydsl.core.types.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.transaction.Transactional

@RestController
@RequestMapping("/feed")
class FeedController(
    val feedRepository: FeedRepository,
    val userRepository: UserRepository
) {

    data class PostFeedReq(
        val title: String,
        val content: String,
        val writerId: UUID,
    )

    @PostMapping("")
    fun postFeed(@RequestBody postFeedReq: PostFeedReq): Feed {
        val user = userRepository.findById(postFeedReq.writerId)
        if (user.isEmpty) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "user is not exist")

        val feed = Feed(UUID.randomUUID(), postFeedReq.title, postFeedReq.content, user.get())
        return feedRepository.save(feed)
    }

    @GetMapping("/{id}")
    fun getFeedById(@PathVariable id: UUID): Feed {
        feedRepository.findById(id).let {
            if (it.isPresent) return it.get() else throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "feed is not exist"
            )
        }
    }

    data class GetFeedsRes(
        val id: UUID,
        val title: String,
        val content: String,
        val writer: Writer
    ){
        data class Writer(
            val id: UUID,
            val username: String
        )
    }

    @GetMapping("")
    @Transactional
    fun getFeeds(
        @QuerydslPredicate(root = Feed::class) predicate: Predicate,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<GetFeedsRes> {
        return feedRepository.findAll(predicate, pageable).map {
            GetFeedsRes(it.id, it.title, it.content, GetFeedsRes.Writer(it.writer.id, it.writer.username))
        }
    }

    data class PutFeedReq(
        val title: String,
        val content: String,
    )

    @PutMapping("/{id}")
    fun putFeed(@PathVariable id: UUID, @RequestBody putFeedReq: PutFeedReq): Feed {
        feedRepository.findById(id).let {
            if (it.isPresent) {
                val feed = it.get().copy(title = putFeedReq.title, content = putFeedReq.content)
                return feedRepository.save(feed)
            } else throw ResponseStatusException(HttpStatus.NOT_FOUND, "feed is not exist")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteFeed(@PathVariable id: UUID) {
        feedRepository.findById(id).let {
            if (it.isPresent) {
                return feedRepository.delete(it.get())
            } else throw ResponseStatusException(HttpStatus.NOT_FOUND, "feed is not exist")
        }
    }
}