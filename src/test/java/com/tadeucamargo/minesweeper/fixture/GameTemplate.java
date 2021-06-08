package com.tadeucamargo.minesweeper.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.tadeucamargo.minesweeper.controller.request.NewGameRequest;
import com.tadeucamargo.minesweeper.entity.Cell;
import com.tadeucamargo.minesweeper.entity.Game;
import com.tadeucamargo.minesweeper.enums.CellStatus;
import com.tadeucamargo.minesweeper.enums.GameStatus;

public class GameTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(NewGameRequest.class).addTemplate("valid", new Rule(){{
            add("columns", 3);
            add("rows", 3);
            add("mines", 2);
            add("user", random("user1","user2","user3"));
        }});
        Fixture.of(Game.class).addTemplate("RUNNING", new Rule(){{
            add("id",1L);
            add("userName","user");
            add("status", GameStatus.RUNNING);
            add("rows",3);
            add("columns",3);
            add("mines",2);
        }});

        Fixture.of(Game.class).addTemplate("FINISH", new Rule(){{
            add("id",1L);
            add("userName","user");
            add("status", GameStatus.FINISH);
            add("rows",3);
            add("columns",3);
            add("mines",2);
        }});

        Fixture.of(Game.class).addTemplate("WON", new Rule(){{
            add("id",1L);
            add("userName","user");
            add("status", GameStatus.WON);
            add("rows",3);
            add("columns",3);
            add("mines",2);
        }});

        Fixture.of(Game.class).addTemplate("LOOSE", new Rule(){{
            add("id",1L);
            add("userName","user");
            add("status", GameStatus.LOOSE);
            add("rows",3);
            add("columns",3);
            add("mines",2);
        }});

        Fixture.of(Cell.class).addTemplate("1", new Rule(){{
            add("id",random(1L,200L));
            add("x",0);
            add("y", 0);
            add("hasMine",false);
            add("status", CellStatus.OPEN);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("2", new Rule(){{
            add("id",random(1L,200L));
            add("x",0);
            add("y", 1);
            add("hasMine",false);
            add("status", CellStatus.CLOSE);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("3", new Rule(){{
            add("id",random(1L,200L));
            add("x",0);
            add("y", 2);
            add("hasMine",true);
            add("status", CellStatus.CLOSE);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("4", new Rule(){{
            add("id",random(1L,200L));
            add("x",1);
            add("y", 0);
            add("hasMine",true);
            add("status", CellStatus.CLOSE);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("5", new Rule(){{
            add("id",random(1L,200L));
            add("x",1);
            add("y", 1);
            add("hasMine",false);
            add("status", CellStatus.CLOSE);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("6", new Rule(){{
            add("id",random(1L,200L));
            add("x",1);
            add("y", 2);
            add("hasMine",false);
            add("status", CellStatus.OPEN);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("7", new Rule(){{
            add("id",random(1L,200L));
            add("x",2);
            add("y", 0);
            add("hasMine",false);
            add("status", CellStatus.QUESTION);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("8", new Rule(){{
            add("id",random(1L,200L));
            add("x",2);
            add("y", 1);
            add("hasMine",false);
            add("status", CellStatus.FLAG);
            add("game",one(Game.class,"RUNNING"));
        }});
        Fixture.of(Cell.class).addTemplate("9", new Rule(){{
            add("id",random(1L,200L));
            add("x",2);
            add("y", 2);
            add("hasMine",false);
            add("status", CellStatus.CLOSE);
            add("game",one(Game.class,"RUNNING"));
        }});


    }
}
