package com.itt.newsAggregation.dto.common;

import com.itt.newsAggregation.model.UserReaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReactionDto {
    private Integer userId;
    private Integer articleId;
    private UserReaction.ReactionType reaction;
}
