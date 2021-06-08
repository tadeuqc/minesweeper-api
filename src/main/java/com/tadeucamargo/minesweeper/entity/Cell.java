package com.tadeucamargo.minesweeper.entity;

import com.tadeucamargo.minesweeper.enums.CellStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "CellSequence", sequenceName = "Cell_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    private int x;
    private int y;
    private boolean hasMine;
    private int adjacentMine;
    private CellStatus status;
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Game game;
}
