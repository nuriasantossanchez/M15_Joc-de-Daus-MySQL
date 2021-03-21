package jocDeDaus.dto;

import jocDeDaus.entity.CrapsRoll;
import jocDeDaus.entity.Player;
import org.springframework.stereotype.Component;

/**
 * Clase de la capa de dominio, implementa el patron Data Transfer Object (DTO Pattern) mediante la
 * creacion de un objeto plano (POJO) con una serie de atributos que puedan ser enviados o recuperados
 * del servidor en una sola invocación (de tal forma que un DTO puede contener información de multiples
 * fuentes o tablas y concentrarlas en una unica clase simple, esto es, crear estructuras de datos
 * independientes del modelo de datos, para transmitir información entre un cliente y un servidor)
 *
 * Anotaciones:
 * @Component
 * Indica que una clase es un "componente".
 * Estas clases se consideran candidatas para la deteccion automatica cuando se utiliza una configuración
 * basada en anotaciones y un escaneo de classpath.
 * También se pueden considerar otras anotaciones a nivel de clase como identificación de un componente,
 * normalmente un tipo especial de componente: por ejemplo, la anotación @Repository
 */
@Component
public class GameDto extends ResponseDto{

    private Long idGame;
    private Long idPlayer;
    private Long IdCrapsRoll;
    private Boolean gameResult;

    public GameDto() {
    }

    public Long getIdGame() {
        return idGame;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }

    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Long getIdCrapsRoll() {
        return IdCrapsRoll;
    }

    public void setIdCrapsRoll(Long idCrapsRoll) {
        IdCrapsRoll = idCrapsRoll;
    }

    public Boolean getGameResult() {
        return gameResult;
    }

    public void setGameResult(Boolean gameResult) {
        this.gameResult = gameResult;
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "idGame=" + idGame +
                ", idPlayer=" + idPlayer +
                ", IdCrapsRoll=" + IdCrapsRoll +
                ", gameResult=" + gameResult +
                '}';
    }
}
