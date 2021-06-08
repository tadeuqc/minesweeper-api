package com.tadeucamargo.minesweeper.controller.response;

import com.tadeucamargo.minesweeper.enums.CellStatus;
import lombok.Data;

@Data
public class CellResponse {
    private CellStatus status;
    private Integer x;
    private Integer y;
    private Integer adjacentMine;
}
