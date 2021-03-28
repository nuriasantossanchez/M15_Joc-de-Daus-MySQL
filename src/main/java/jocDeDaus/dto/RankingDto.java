package jocDeDaus.dto;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Comparator.comparing;

/**
 * Clase de la capa de dominio, implementa el patron Data Transfer Object (DTO Pattern) mediante la
 * creacion de un objeto plano (POJO) con una serie de atributos que puedan ser enviados o recuperados
 * del servidor en una sola invocacion (de tal forma que un DTO puede contener informacion de multiples
 * fuentes o tablas y concentrarlas en una unica clase simple, esto es, crear estructuras de datos
 * independientes del modelo de datos, para transmitir informacion entre un cliente y un servidor)
 *
 * Anotaciones:
 * @Component
 * Indica que una clase es un "componente".
 * Estas clases se consideran candidatas para la deteccion automatica cuando se utiliza una configuracion
 * basada en anotaciones y un escaneo de classpath.
 * Tambien se pueden considerar otras anotaciones a nivel de clase como identificacion de un componente,
 * normalmente un tipo especial de componente: por ejemplo, la anotacion @Repository
 */

@Component
public class RankingDto extends ResponseDto{

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
