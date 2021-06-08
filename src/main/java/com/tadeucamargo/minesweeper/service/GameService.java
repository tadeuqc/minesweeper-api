package com.tadeucamargo.minesweeper.service;

import com.tadeucamargo.minesweeper.controller.request.NewGameRequest;
import com.tadeucamargo.minesweeper.controller.response.CellResponse;
import com.tadeucamargo.minesweeper.controller.response.GameResponse;
import com.tadeucamargo.minesweeper.entity.Cell;
import com.tadeucamargo.minesweeper.entity.Game;
import com.tadeucamargo.minesweeper.enums.Action;
import com.tadeucamargo.minesweeper.enums.CellStatus;
import com.tadeucamargo.minesweeper.enums.GameStatus;
import com.tadeucamargo.minesweeper.exception.HttpException;
import com.tadeucamargo.minesweeper.repository.CellRepository;
import com.tadeucamargo.minesweeper.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GameService {

    @Autowired
    private GameRepository repository;
    @Autowired
    private CellRepository cellRepository;

    public GameResponse createGame(NewGameRequest newGame){
        Game game = new Game();
        game.setMines(newGame.getMines());
        game.setColumns(newGame.getColumns());
        game.setRows(newGame.getRows());
        game.setUserName(newGame.getUser());
        game.setStatus(GameStatus.RUNNING);
        game.setCreated(new Date());
        game.setCells(createCells(newGame.getMines(), newGame.getRows(), newGame.getRows(),game));

        game = repository.save(game);
        return convert(game,true);
    }

    public GameResponse endGame(Long id){
        Optional<Game> gameOp = repository.findById(id);
        if(gameOp.isPresent()){
            Game game = gameOp.get();
            if(game.getStatus() != GameStatus.RUNNING){
                throw new HttpException(400,"Only Running games can be ended");
            }
            game.setStatus(GameStatus.FINISH);
            game = repository.save(game);
            return convert(game,true);
        }
        throw new HttpException(404,"Not Found");
    }

    public List<GameResponse> findAllByUserName(String username){
        List<GameResponse> listResponse = new ArrayList<>();
        repository.findByUserName(username).stream().forEach(game -> {
            if(game.getStatus().equals(GameStatus.RUNNING))
            listResponse.add(convert(game,false));
        });
        return listResponse;
    }

    public GameResponse findAllById(Long id){
        Optional<Game> gameOp = repository.findById(id);
        if(gameOp.isPresent()) {
            return convert(gameOp.get(), true);
        }
        throw new HttpException(404,"Not Found");
    }


    private GameResponse convert(Game game, boolean hasCell){
        GameResponse response = new GameResponse();
        response.setId(game.getId());
        response.setUserName(game.getUserName());
        response.setColumns(game.getColumns());
        response.setRows(game.getRows());
        response.setMines(game.getMines());
        response.setStatus(game.getStatus());
        response.setCells(new ArrayList<>());
        if(hasCell) {
            game.getCells().stream().forEach(cell ->
            {
                CellResponse cellResponse = new CellResponse();
                cellResponse.setStatus(cell.getStatus());
                if (cell.getStatus() == CellStatus.OPEN) {
                    cellResponse.setAdjacentMine(cell.getAdjacentMine());
                }
                //show all mines when loose
                if(game.getStatus() == GameStatus.LOOSE && cell.isHasMine()){
                cellResponse.setStatus(CellStatus.HAS_MINE);
                }
                //show all mines when finish
                if(game.getStatus() == GameStatus.FINISH && cell.isHasMine()){
                    cellResponse.setStatus(CellStatus.HAS_MINE);
                }

                if(game.getStatus() == GameStatus.WON){
                    cellResponse.setStatus(cell.getStatus());
                    if(cell.isHasMine()){
                        cellResponse.setStatus(CellStatus.HAS_MINE);
                    }
                    cellResponse.setAdjacentMine(cell.getAdjacentMine());
                }
                cellResponse.setX(cell.getX());
                cellResponse.setY(cell.getY());
                response.getCells().add(cellResponse);
            });
        }
        return response;
    }

    private List<Cell> createCells(int mines,int rows,int columns, Game game){
        List<Cell> cells = new ArrayList<>();
        List<Integer> minesArray = generateMines(mines,rows,columns);
        int count = 1;
        for(int x=0;x<rows;x++){
            for(int y=0;y<columns;y++){
                Cell cell = new Cell();
                cell.setX(x);
                cell.setY(y);
                cell.setStatus(CellStatus.CLOSE);
                cell.setHasMine(minesArray.contains(count));
                cell.setGame(game);
                count = count + 1;
                cells.add(cell);
            }
        }

        for (Cell cell:cells) {
            cell.setAdjacentMine(countMineNear(cells,cell.getX(),cell.getY()));
        }
        return cells;
    }

    private List<Integer> generateMines(int mines,int rows,int columns){
        List<Integer> minesList = new ArrayList<>();
        for(int i=0;i<mines;i++){
            minesList.add(generateUniqueNumber(minesList,rows*columns));
        }
        return minesList;
    }

    private int generateUniqueNumber(List<Integer> list,double max){
        double x = (Math.random() * ((max - 0) + 1)) + 1;
        int x1 = (int)x;
        if(!list.stream().filter(integer -> integer == x1).findFirst().isPresent()){
            return x1;
        }
        return generateUniqueNumber(list, max);
    }

    private int countMineNear(List<Cell>cells,int x,int y){
        //its mine
        if(cells.stream().filter(c->(c.getX() == x && c.getY() ==y && c.isHasMine())).findFirst().isPresent()){
            return -1;
        }
        int minesNear= 0;
        minesNear += cells.stream().filter(c->(c.getX() == x-1 && c.getY() ==y-1 && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x-1 && c.getY() ==y && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x-1 && c.getY() ==y+1 && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x && c.getY() ==y-1 && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x && c.getY() ==y+1 && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x+1 && c.getY() ==y-1 && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x+1 && c.getY() ==y && c.isHasMine())).findFirst().isPresent()?1:0;
        minesNear += cells.stream().filter(c->(c.getX() == x+1 && c.getY() ==y+1 && c.isHasMine())).findFirst().isPresent()?1:0;
        return minesNear;
    }

    public GameResponse makeMove(Long id, Action action, int x, int y) {
        Optional<Game> gameOp = repository.findById(id);
        if(!gameOp.isPresent()) {
            throw new HttpException(404,"Game not found");
        }
        Game game = gameOp.get();
        if(game.getStatus() != GameStatus.RUNNING){
            throw new HttpException(400,"You only can make moves on a running game");
        }
        Optional<Cell> cellOp = game.getCells().stream().filter(cell -> (cell.getX() == x && cell.getY() == y)).findFirst();
        if(!cellOp.isPresent()){
            throw new HttpException(404,"Cell not found");
        }

        Cell cell = cellOp.get();
        if(action == Action.FLAG){
            if(cell.getStatus() == CellStatus.OPEN || cell.getStatus() == CellStatus.HAS_MINE){
                throw new HttpException(400,"you can only FLAG closed cells");
            }
            if(cell.getStatus() == CellStatus.CLOSE){
                cell.setStatus(CellStatus.FLAG);
            }else if(cell.getStatus() == CellStatus.FLAG){
                cell.setStatus(CellStatus.QUESTION);
            }else if(cell.getStatus() == CellStatus.QUESTION){
                cell.setStatus(CellStatus.CLOSE);
            }
            cellRepository.save(cell);
        }else{
            if(cell.getStatus() != CellStatus.CLOSE){
                throw new HttpException(400,"you can only FLAG closed cells");
            }
            if(cell.isHasMine()){
                game.setStatus(GameStatus.LOOSE);
                repository.save(game);
            }else{
                setAdjacentsCellsOpen(game.getCells(),x,y);
            }

        }
        Game gameResponse = repository.findById(id).get();

        long openOrFlag = gameResponse.getCells().stream().filter(cell1 -> cell1.getStatus() == CellStatus.OPEN).count();
        if(openOrFlag== ((game.getRows() * game.getColumns())-game.getMines())){
            game.setStatus(GameStatus.WON);
            gameResponse = repository.save(game);
        }



        return convert(gameResponse,true);
    }
    
    private void setAdjacentsCellsOpen(List<Cell> cells, int x, int y){
        Optional<Cell> cellOp = cells.stream().filter(cell -> (cell.getX() == x && cell.getY() == y)).findFirst();
        if(cellOp.isPresent()){
            Cell cell = cellOp.get();
            if(cell.getStatus() == CellStatus.CLOSE && !cell.isHasMine()){
                cell.setStatus(CellStatus.OPEN);
                cellRepository.save(cell);
                cells = cellRepository.findByGame(cell.getGame().getId());
                if(cell.getAdjacentMine() == 0) {
                    setAdjacentsCellsOpen(cells, x - 1, y - 1);
                    setAdjacentsCellsOpen(cells, x - 1, y);
                    setAdjacentsCellsOpen(cells, x - 1, y + 1);
                    setAdjacentsCellsOpen(cells, x, y - 1);
                    setAdjacentsCellsOpen(cells, x, y + 1);
                    setAdjacentsCellsOpen(cells, x + 1, y - 1);
                    setAdjacentsCellsOpen(cells, x + 1, y);
                    setAdjacentsCellsOpen(cells, x + 1, y + 1);
                }
            }
        }
    }
}
