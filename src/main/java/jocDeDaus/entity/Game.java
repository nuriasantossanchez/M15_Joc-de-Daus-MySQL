package jocDeDaus.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Clase de la capa de dominio.
 *
 * La anotacion @Entity indica que la clase es una entidad.
 *
 * La anotacion @Table indica que la clase sera mapeada a una tabla y persistida
 *
 * Entidad que contiene clave primaria compuesta, anotada con @IdClass(GamePlayerPk.class)
 *
 */

@Entity
@Table(name="GAME") //en caso que la tabla sea diferente
@IdClass(GamePlayerPk.class)
public class Game implements Serializable {

    //Atributos de entidad Game
    @Id
    @Column(name = "ID_GAME")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idGame;

    @Id
    @Column(name = "ID_PLAYER")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPlayer;

    @Column(name = "ID_CRAPSROLL")
    private Long IdCrapsRoll;

    @Column(name = "GAME_RESULT")
    private Boolean gameResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_PLAYER", insertable = false, updatable = false)
    private Player player;

    @OneToOne
    @JoinColumn(name = "ID_CRAPSROLL", insertable = false, updatable = false)
    private CrapsRoll crapsRoll;


    public Game() {
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CrapsRoll getCrapsRoll() {
        return crapsRoll;
    }

    public void setCrapsRoll(CrapsRoll crapsRoll) {
        this.crapsRoll = crapsRoll;
    }
}
