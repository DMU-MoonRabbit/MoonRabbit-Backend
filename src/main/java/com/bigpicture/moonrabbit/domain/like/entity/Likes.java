package com.bigpicture.moonrabbit.domain.like.entity;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Like {
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
