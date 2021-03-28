package jocDeDaus.util;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Game;
import jocDeDaus.entity.Player;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.reducing;

/**
 * Clase de la capa de Utilidades
 *
 * Implementa la interface IUtilities
 *
 */

@Component
public class ControllerUtilities implements IUtilities {

    ToDoubleBiFunction<Double, Double> computeAverageFunction = (a, b) -> a/b*100;

    public Optional<Player> checkUniqueNickName(Player newPlayer, List<Player> players) {

        return players.stream()
                .filter(p -> !p.getName().toUpperCase().equals("ANONYMOUS")
                        && !newPlayer.getName().isBlank()
                        && p.getName().toUpperCase().contains(newPlayer.getName().trim().toUpperCase()))
                .findFirst();
    }

    public Game generateNewGame(Player player) {
        Game game = new Game();

        Long maxValueGamePlayer = player.getCrapsRolls().stream()
                .map(cr -> cr.getIdCrapsRoll())
                .max(Comparator.naturalOrder()).orElseGet(()-> 0L);

        game.setIdGame(maxValueGamePlayer+1);

        game.setIdPlayer(player.getIdPlayer());
        return game;
    }

    public CrapsRoll generateNewCrapsRoll(Long idPlayer) {
        CrapsRoll crapsRoll = new CrapsRoll();

        crapsRoll.setIdPlayer(idPlayer);

        List<Short> randomCrapsRoll = getRandomNumbers();

        crapsRoll.setCrapOne(randomCrapsRoll.get(0));
        crapsRoll.setCrapTwo(randomCrapsRoll.get(1));
        crapsRoll.setRollResult((short) (crapsRoll.getCrapOne()
                +crapsRoll.getCrapTwo()));
        return crapsRoll;
    }

    public List<Short> getRandomNumbers(){

        Random random = new Random();
        Integer max = 6;
        Integer min = 1;

        List<Short> randomNumbers = Stream.generate(()->random.nextInt((max - min) + 1) + min)
                .distinct()
                .limit(2)
                .map(r -> r.shortValue())
                .collect(toList());

        return randomNumbers;
    }

    public Double computePlayerSuccessRanking(List<CrapsRoll> crapsRolls) {

        double successRanking = Double.valueOf(0);

        if (null != crapsRolls && !crapsRolls.isEmpty()){

            Long wins = getWinsCrapsRolls(crapsRolls);

            successRanking = computeAverageFunction.applyAsDouble((double) wins, (double)crapsRolls.stream().count());

            return Math.round(successRanking*100.0)/100.0;
        }

        return successRanking;
    }

    public Long getWinsCrapsRolls(List<CrapsRoll> crapsRolls) {

        Long wins = crapsRolls.stream()
                .filter(r -> r.getRollResult() == 7)
                .count();
        return wins;
    }

    public Double computeAverageRankingAllPlayers(List<Game> games) {

        Map<Long, Long> gamesByPlayer = getGamesByPlayer(games);

        Long wins = getWinsGames(games);

        Long totalGames = gamesByPlayer.values().stream().reduce(Long::sum).get();

        double averageRanking = computeAverageFunction.applyAsDouble((double) wins, (double) totalGames)
                /gamesByPlayer.size();

        return Math.round(averageRanking*100.0)/100.0;
    }

    public Long getWinsGames(List<Game> games) {
        Long wins =
                games.stream()
                        .filter(g -> g.getGameResult() == true)
                        .collect(counting());
        return wins;
    }

    public Map<Long, Long> getGamesByPlayer(List<Game> games) {
        Map<Long, Long> gamesByPlayer = games.stream().collect(
                groupingBy(Game::getIdPlayer, counting()));

        return gamesByPlayer;
    }

    public Double computeGameSuccessRanking(List<Game> games) {
        double successRanking = Double.valueOf(0);
        if (null != games && !games.isEmpty()){
            Long wins = getWinsGames(games);

            successRanking = computeAverageFunction.applyAsDouble((double) wins, (double)games.stream().count());

            return Math.round(successRanking*100.0)/100.0;
        }
        return successRanking;
    }

    public Optional<Player> getWorstPlayer(List<Player> players) {
        players.forEach(p -> p.setRanking(computeGameSuccessRanking(p.getGames())));

        return Optional.of(players.stream()
                .collect(reducing((p1, p2) -> p1.getRanking() < p2.getRanking() ? p1 : p2)).get());
    }

    public Optional<Player> getBestPlayer(List<Player> players) {
        players.forEach(p -> p.setRanking(computeGameSuccessRanking(p.getGames())));

        return Optional.of(players.stream()
                .collect(reducing((p1, p2) -> p1.getRanking() > p2.getRanking() ? p1 : p2)).get());
    }
}
