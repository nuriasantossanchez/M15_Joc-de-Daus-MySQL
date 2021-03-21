package jocDeDaus.service;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interface de la capa Service
 *
 */
public interface IGameService {

    Long countAll();

    List<Game> allGames(); // average success ranking of all players

    Player computeLoserRanking(); // player with worst success rate

    Player computeWinnerRanking(); // player with best success rate

}
