package com.tadeucamargo.minesweeper;

import com.tadeucamargo.minesweeper.controller.GameController;
import com.tadeucamargo.minesweeper.controller.request.NewGameRequest;
import com.tadeucamargo.minesweeper.controller.response.GameResponse;
import com.tadeucamargo.minesweeper.enums.Action;
import com.tadeucamargo.minesweeper.service.GameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {MinesweeperApplication.class})
@ActiveProfiles("test")
public class GameControllerTests {

    @InjectMocks
    GameController controller;

    @Mock
    private GameService service;

    private Long id = 10L;

    @Test
    public void createTest(){
        NewGameRequest request = new NewGameRequest();
        Mockito.doReturn(new GameResponse()).when(service).createGame(request);
        Assert.assertNotNull(controller.create(request));
    }
    @Test
    public void endTest(){
        Mockito.doReturn(new GameResponse()).when(service).endGame(id);
        Assert.assertNotNull(controller.end(id));
    }

    @Test
    public void makeMoveTest(){
        Mockito.doReturn(new GameResponse()).when(service).makeMove(id, Action.FLAG,0,0);
        Assert.assertNotNull(controller.makeMove(id, Action.FLAG,0,0));
    }

    @Test
    public void listAllGameTest(){
        Mockito.doReturn(new ArrayList<>()).when(service).findAllByUserName("username");
        Assert.assertNotNull(controller.listAllGame("username"));
    }

    @Test
    public void getTest(){
        Mockito.doReturn(new GameResponse()).when(service).findAllById(id);
        Assert.assertNotNull(controller.get(id));
    }
}
