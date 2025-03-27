package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.dto.ReactionDTO;
import org.example.entity.Reaction;

@UtilityClass
public class ReactionMapper {

    public ReactionDTO convertToDto(Reaction reaction) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(reaction.getId());
        reactionDTO.setArticleId(reaction.getArticle() != null ? reaction.getArticle().getId() : null);
        reactionDTO.setType(reaction.getType());
        reactionDTO.setUser_id(reaction.getUser() != null ? reaction.getUser().getId() : null);


        return reactionDTO;
    }

    public Reaction convertToEntity(ReactionDTO reactionDTO) {
        Reaction reaction = new Reaction();
        reaction.setId(reactionDTO.getId());
        reaction.setType(reactionDTO.getType());
        reactionDTO.setUser_id(reaction.getUser() != null ? reaction.getUser().getId() : null);

        return reaction;
    }
}