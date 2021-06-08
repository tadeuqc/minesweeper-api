package com.tadeucamargo.minesweeper.entity;

import com.tadeucamargo.minesweeper.enums.GameStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "GameSequence", sequenceName = "Game_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    private String userName;
    private GameStatus status;
    private int rows;
    private int columns;
    private int mines;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChange;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<Cell> cells;
}
