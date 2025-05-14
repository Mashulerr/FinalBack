package org.example.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
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

    private int likes;
    private int dislikes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteArticle> favoriteArticles = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Метод для подсчета лайков
    public int getLikes() {
        return (int) reactions.stream()
                .filter(reaction -> "like".equals(reaction.getType()))
                .count();
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int likes) {
        this.dislikes=dislikes;
    }



    public int getDislikes() {
        return (int) reactions.stream()
                .filter(reaction -> "dislike".equals(reaction.getType()))
                .count();
    }
}