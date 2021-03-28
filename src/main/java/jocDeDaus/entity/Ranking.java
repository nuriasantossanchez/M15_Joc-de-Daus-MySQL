package jocDeDaus.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Clase de la capa de dominio.
 *
 */

public class Ranking implements Serializable {

    private Double averageRankingAllPlayers;
    private List<Game> games;

    public Ranking() {
    }

    public Double getAverageRankingAllPlayers() {
        return averageRankingAllPlayers;
    }

    public void setAverageRankingAllPlayers(Double averageRankingAllPlayers) {
        this.averageRankingAllPlayers = averageRankingAllPlayers;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
