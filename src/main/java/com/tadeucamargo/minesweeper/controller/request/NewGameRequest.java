package com.tadeucamargo.minesweeper.controller.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewGameRequest implements Serializable {
    private int columns;
    private int rows;
    private int mines;
    private String user;
}
