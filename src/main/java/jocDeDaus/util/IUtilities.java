package jocDeDaus.util;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface IUtilities {

    Optional<Player> checkUniqueNickName(Player newPlayer, List<Player> players);
    Game generateNewGame(Player player);
    CrapsRoll generateNewCrapsRoll(Long idPlayer);
    List<Short> getRandomNumbers();
    Double computePlayerSuccessRanking(List<CrapsRoll> crapsRolls);
    Long getWinsCrapsRolls(List<CrapsRoll> crapsRolls);
    Double computeAverageRankingAllPlayers(List<Game> games);
    Long getWinsGames(List<Game> games);
    Map<Long, Long> getGamesByPlayer(List<Game> games);
    Double computeGameSuccessRanking(List<Game> games);
    Optional<Player> getWorstPlayer(List<Player> players);
    Optional<Player> getBestPlayer(List<Player> players);
}
