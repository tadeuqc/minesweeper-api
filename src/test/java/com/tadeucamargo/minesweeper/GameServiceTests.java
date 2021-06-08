package com.tadeucamargo.minesweeper;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.tadeucamargo.minesweeper.controller.request.NewGameRequest;
import com.tadeucamargo.minesweeper.entity.Cell;
import com.tadeucamargo.minesweeper.entity.Game;
import com.tadeucamargo.minesweeper.enums.Action;
import com.tadeucamargo.minesweeper.enums.GameStatus;
import com.tadeucamargo.minesweeper.exception.HttpException;
import com.tadeucamargo.minesweeper.repository.CellRepository;
import com.tadeucamargo.minesweeper.repository.GameRepository;
import com.tadeucamargo.minesweeper.service.GameService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {MinesweeperApplication.class})
@ActiveProfiles("test")
public class GameServiceTests {
    @InjectMocks
    GameService service;

    @Mock
    private GameRepository repository;
    @Mock
    private CellRepository cellRepository;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.tadeucamargo.minesweeper.fixture");
    }

    @Test
    public void createGameTest(){
        NewGameRequest request = Fixture.from(NewGameRequest.class).gimme("valid");
        Mockito.doReturn(createGame("RUNNING")).when(repository).save(any());
        Assert.assertNotNull(service.createGame(request));
    }

    @Test(expected = HttpException.class)
    public void endGameNotFoundTest(){
        Mockito.doReturn(Optional.empty()).when(repository).findById(any());
        service.endGame(10L);
    }

    @Test(expected = HttpException.class)
    public void endGameNotRunningTest(){
        Mockito.doReturn(Optional.of(Fixture.from(Game.class).gimme("FINISH"))).when(repository).findById(any());
        service.endGame(10L);
    }

    @Test
    public void endGameTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        Mockito.doReturn(createGame("RUNNING")).when(repository).save(any());
        Assert.assertNotNull(service.endGame(10L));
    }

    @Test
    public void findAllByUserNameTest(){
        Mockito.doReturn(Fixture.from(Game.class).gimme(2,"RUNNING")).when(repository).findByUserName(anyString());
        Assert.assertNotNull(service.findAllByUserName("username"));
    }

    @Test(expected = HttpException.class)
    public void findAllByIdNotFoundTest(){
        Mockito.doReturn(Optional.empty()).when(repository).findById(any());
        service.findAllById(10l);
    }

    @Test
    public void findAllByIdTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        service.findAllById(10l);
    }

    @Test
    public void findAllByIdWonTest(){
        Mockito.doReturn(Optional.of(createGame("WON"))).when(repository).findById(any());
        Assert.assertNotNull(service.findAllById(10l));
    }

    @Test
    public void findAllByIdLooseTest(){
        Mockito.doReturn(Optional.of(createGame("LOOSE"))).when(repository).findById(any());
        Assert.assertNotNull(service.findAllById(10l));
    }

    @Test
    public void findAllByIdFinishTest(){
        Mockito.doReturn(Optional.of(createGame("FINISH"))).when(repository).findById(any());
        Assert.assertNotNull(service.findAllById(10l));
    }
    @Test(expected = HttpException.class)
    public void makeMoveGameNotFoundTest(){
        Mockito.doReturn(Optional.empty()).when(repository).findById(any());
        service.makeMove(10L, Action.FLAG,0,0);
    }

    @Test(expected = HttpException.class)
    public void makeMoveStatusNotRunningTest(){
        Mockito.doReturn(Optional.of(createGame("FINISH"))).when(repository).findById(any());
        service.makeMove(10L, Action.FLAG,0,0);
    }

    @Test(expected = HttpException.class)
    public void makeMoveStatusCellNotFoundTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        service.makeMove(10L, Action.FLAG,10,0);
    }

    @Test(expected = HttpException.class)
    public void makeMoveStatusCellStatusTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        service.makeMove(10L, Action.FLAG,0,0);
    }

    @Test
    public void makeMoveStatusCloseTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        Mockito.doReturn(Fixture.from(Cell.class).gimme("1")).when(cellRepository).save(any());
        service.makeMove(10L, Action.FLAG,2,2);
    }
    @Test
    public void makeMoveStatusFlagTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        Mockito.doReturn(Fixture.from(Cell.class).gimme("1")).when(cellRepository).save(any());
        service.makeMove(10L, Action.FLAG,2,1);
    }
    @Test
    public void makeMoveStatusQuestionTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        Mockito.doReturn(Fixture.from(Cell.class).gimme("1")).when(cellRepository).save(any());
        service.makeMove(10L, Action.FLAG,2,0);
    }

    @Test(expected = HttpException.class)
    public void makeMovePlayTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        service.makeMove(10L, Action.PLAY,1,2);
    }

    @Test
    public void makeMovePlayLooseTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        Assert.assertEquals(GameStatus.LOOSE,service.makeMove(10L, Action.PLAY,1,0).getStatus());
    }

    @Test
    public void makeMovePlayPlayTest(){
        Mockito.doReturn(Optional.of(createGame("RUNNING"))).when(repository).findById(any());
        Assert.assertNotNull(service.makeMove(10L, Action.PLAY,0,1));
    }

    @Test
    public void makeMovePlayWonTest(){
        Game game = Fixture.from(Game.class).gimme("RUNNING");
        game.setCells(new ArrayList<>());
        game.getCells().add(Fixture.from(Cell.class).gimme("9"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("4"));
        game.getCells().add(Fixture.from(Cell.class).gimme("4"));
        Mockito.doReturn(Optional.of(game)).when(repository).findById(any());
        Mockito.doReturn(game).when(repository).save(any());
        Assert.assertEquals(GameStatus.WON,service.makeMove(10L, Action.PLAY,2,2).getStatus());
    }


    private Game createGame(String status){
        Game game = Fixture.from(Game.class).gimme(status);
        game.setCells(new ArrayList<>());
        game.getCells().add(Fixture.from(Cell.class).gimme("1"));
        game.getCells().add(Fixture.from(Cell.class).gimme("2"));
        game.getCells().add(Fixture.from(Cell.class).gimme("3"));
        game.getCells().add(Fixture.from(Cell.class).gimme("4"));
        game.getCells().add(Fixture.from(Cell.class).gimme("5"));
        game.getCells().add(Fixture.from(Cell.class).gimme("6"));
        game.getCells().add(Fixture.from(Cell.class).gimme("7"));
        game.getCells().add(Fixture.from(Cell.class).gimme("8"));
        game.getCells().add(Fixture.from(Cell.class).gimme("9"));
        return game;
    }
}
