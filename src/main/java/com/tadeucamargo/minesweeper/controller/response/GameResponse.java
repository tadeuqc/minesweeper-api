package com.tadeucamargo.minesweeper.controller.response;

import com.tadeucamargo.minesweeper.enums.GameStatus;
import lombok.Data;

import java.util.List;

@Data
public class GameResponse {
    private Long id;
    private String userName;
    private Integer rows;
    private Integer columns;
    private Integer mines;
    private GameStatus status;
    private List<CellResponse> cells;
}
