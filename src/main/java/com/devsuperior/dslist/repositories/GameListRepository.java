package com.devsuperior.dslist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dslist.entitites.GameList;

public interface GameListRepository extends JpaRepository<GameList, Long> {
	
	/* Native SQL query with UPDATE, DELETE, INSERT - when function is not SELECT, we need '@Modifying' 
	 * Here, when given a list and game id, it updates the position in the db */
	@Modifying
	@Query(nativeQuery = true, 
	value = "UPDATE tb_belonging SET position = :newPosition WHERE list_id = :listId AND game_id = :gameId")
	void updateBelongingPosition(Long listId, Long gameId, Integer newPosition);
	
}
