package com.tadeucamargo.minesweeper.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HttpException extends RuntimeException{
    private int httpCode;
    private String message;

}
