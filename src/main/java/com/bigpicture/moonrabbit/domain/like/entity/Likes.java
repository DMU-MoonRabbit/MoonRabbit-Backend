package com.bigpicture.moonrabbit.domain.like.entity;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table( name = "likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "board_id"})
)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
