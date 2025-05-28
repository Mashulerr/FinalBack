package org.example.dto;


import lombok.Data;
import org.example.entity.ReactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ReactionDTO {
    private Long id;
    private Long articleId;
    private Long userId;

    @NotBlank(message = "Reaction type is required")
    @Pattern(regexp = "like|dislike", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Reaction type must be either 'like' or 'dislike'")
    private String type;



}