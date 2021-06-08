package com.tadeucamargo.minesweeper.repository;

import com.tadeucamargo.minesweeper.entity.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game,Long> {
    @Query("SELECT g FROM Game g join fetch g.cells c WHERE g.id = :id")
    Game findByIdFetchCell(Long id);

    List<Game> findByUserName(String userName);
}
