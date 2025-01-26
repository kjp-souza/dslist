package com.devsuperior.dslist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entitites.GameList;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.GameRepository;

/* OR @Component */
@Service
public class GameListService {
	
	@Autowired 
	private GameListRepository gameListRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Transactional(readOnly = true)
	public List<GameListDTO> findAll(){
		/* List<Game> OR var */
		List<GameList> result = gameListRepository.findAll();
		List<GameListDTO> dto = result.stream().map(x -> new GameListDTO(x)).toList();
		return dto;		
	}
	
	/* All this happens in a transaction and we should guarantee all will be executed with @Transactional */
	@Transactional
	public void move(Long listId, int sourceIndex, int destinationIndex) {
		List<GameMinProjection> list = gameRepository.searchByList(listId);
		
		/* Removes object from one position and puts in another in the list */
		GameMinProjection obj = list.remove(sourceIndex);
		list.add(destinationIndex, obj);
		
		/* Find the minimum value:
		 * If source is less than destination, then '?' source is the minimum value. If not ':', it is the destination  */
		int min = sourceIndex < destinationIndex ? sourceIndex : destinationIndex;
		/* Find the minimum value: */
		int max = sourceIndex < destinationIndex ? destinationIndex : sourceIndex;
		
		for (int i = min; i <= max; i++) {
			/* Loop within a certain listId gets a game ID in its current list position and updates position (i):
			 * listId = list
			 * list.get(i) = game id and position
			 * (i) new position */
			gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i); 
		}
	}
}
