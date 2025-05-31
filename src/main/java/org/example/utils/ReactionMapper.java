package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.ReactionDTO;
import org.example.entity.Reaction;

@UtilityClass
public class ReactionMapper {
    public ReactionDTO convertToDto(Reaction reaction) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(reaction.getId());
        reactionDTO.setArticleId(reaction.getArticle().getId());
        reactionDTO.setUserId(reaction.getUser().getId());
        reactionDTO.setType(reaction.getType().name().toLowerCase()); // Возвращаем в нижнем регистре
        return reactionDTO;
    }

    public Reaction convertToEntity(ReactionDTO reactionDTO) {
        Reaction reaction = new Reaction();
        reaction.setId(reactionDTO.getId());

        return reaction;
    }
}