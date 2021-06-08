package com.tadeucamargo.minesweeper.controller;

import com.tadeucamargo.minesweeper.controller.request.NewGameRequest;
import com.tadeucamargo.minesweeper.controller.response.GameResponse;
import com.tadeucamargo.minesweeper.entity.Game;
import com.tadeucamargo.minesweeper.enums.Action;
import com.tadeucamargo.minesweeper.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/game")
@Api(value = "GameController")
public class GameController {
    @Autowired
    private GameService service;

    @PostMapping
    @ApiOperation(value = "Create new game")
    public ResponseEntity<GameResponse> create(@RequestBody NewGameRequest request){
        return ResponseEntity.ok(service.createGame(request));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove game")
    public ResponseEntity<GameResponse> end(@PathVariable Long id){
        return ResponseEntity.ok(service.endGame(id));
    }

    @PatchMapping("/{id}/{action}/{x}/{y}")
    @ApiOperation(value = "Make a move or flag")
    public ResponseEntity<GameResponse> makeMove(@PathVariable Long id, @PathVariable Action action,@PathVariable int x, @PathVariable int y ){
        return ResponseEntity.ok(service.makeMove(id,action,x,y));
    }

    @RequestMapping("/all/{username}")
    @ApiOperation(value = "List all running game from user")
    public ResponseEntity<List<GameResponse>> listAllGame(@PathVariable String username){
        return ResponseEntity.ok(service.findAllByUserName(username));
    }

    @RequestMapping("/{id}")
    @ApiOperation(value = "get information game")
    public ResponseEntity<GameResponse>  get(@PathVariable Long id){
        return ResponseEntity.ok(service.findAllById(id));
    }
}
