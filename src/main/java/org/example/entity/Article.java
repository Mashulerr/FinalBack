package org.example.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reaction> reactions;

    // Метод для подсчета лайков
    public int getLikes() {
        return (int) reactions.stream()
                .filter(reaction -> "like".equals(reaction.getType()))
                .count();
    }

    // Метод для подсчета дизлайков
    public int getDislikes() {
        return (int) reactions.stream()
                .filter(reaction -> "dislike".equals(reaction.getType()))
                .count();
    }
}