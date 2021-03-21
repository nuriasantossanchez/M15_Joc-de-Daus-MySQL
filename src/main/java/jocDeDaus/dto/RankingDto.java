package jocDeDaus.dto;

import jocDeDaus.entity.Game;

import java.util.List;

import static java.util.Comparator.comparing;

public class RankingDto {

    private Double averageRankingAllPlayers;
    private List<GameDto> games;

    public RankingDto() {

    }

    public Double getAverageRankingAllPlayers() {
        return averageRankingAllPlayers;
    }

    public void setAverageRankingAllPlayers(Double averageRankingAllPlayers) {
        this.averageRankingAllPlayers = averageRankingAllPlayers;
    }

    public List<GameDto> getGames() {
        games.sort(comparing(GameDto::getIdPlayer));
        return games;
    }

    public void setGames(List<GameDto> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "RankingDto {" +
                "averageRankingAllPlayers=" + averageRankingAllPlayers +
                ", games=" + games +
                '}';
    }
}
