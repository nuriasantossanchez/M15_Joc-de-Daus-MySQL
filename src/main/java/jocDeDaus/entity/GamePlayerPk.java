package jocDeDaus.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Clase de la capa de dominio.
 *
 * Clase de clave primaria, utilizada para representar una clave primaria compuesta.
 *
 * Sus campos o propiedades deben ser declarados exactamente igual que los campos o propiedades
 * de la entidad que contiene la clave primaria compuesta, en este caso, la entidad Game, anotada
 * con @IdClass(GamePlayerPk.class)
 */
public class GamePlayerPk implements Serializable {

    @Column(name = "ID_GAME")
    private Long idGame;

    @Column(name = "ID_PLAYER")
    private Long idPlayer;

    public GamePlayerPk() {
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
}
