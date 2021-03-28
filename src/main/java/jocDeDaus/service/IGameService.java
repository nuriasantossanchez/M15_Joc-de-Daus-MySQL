package jocDeDaus.service;

import jocDeDaus.entity.Game;
import java.util.List;

/**
 * Interface de la capa Service
 *
 */

public interface IGameService {

    Long countAll(); // count all games

    List<Game> allGames(); // get all games
}
