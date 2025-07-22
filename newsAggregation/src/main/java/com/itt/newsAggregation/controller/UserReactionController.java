package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.common.UserReactionDto;
import com.itt.newsAggregation.service.UserReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class UserReactionController {

    private final UserReactionService reactionService;

    @PostMapping
    public ResponseEntity<?> addOrUpdateReaction(@RequestBody UserReactionDto dto) {
        boolean success = reactionService.addOrUpdateReaction(dto);
        if (success) {
            return ResponseEntity.ok("✅ Reaction saved/updated.");
        } else {
            return ResponseEntity.status(200).body("⚠️ Reaction already exists with same type.");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeReaction(@RequestBody UserReactionDto dto) {
        reactionService.removeReaction(dto);
        return ResponseEntity.ok("Reaction removed");
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<Map<String, Long>> getReactionsForArticle(@PathVariable Integer articleId) {
        return ResponseEntity.ok(reactionService.getReactionCountsForArticle(articleId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReactionDto>> getReactionsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(reactionService.getUserReactions(userId));
    }
}