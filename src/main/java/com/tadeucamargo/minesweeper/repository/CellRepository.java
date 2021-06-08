package com.tadeucamargo.minesweeper.repository;

import com.tadeucamargo.minesweeper.entity.Cell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends CrudRepository<Cell,Long> {
    @Query("SELECT c FROM Cell c where c.game.id =:gameId")
    List<Cell> findByGame(Long gameId);
}
