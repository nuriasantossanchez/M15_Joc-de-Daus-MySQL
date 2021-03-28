package jocDeDaus.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Clase de la capa de dominio.
 *
 * La anotacion @Entity indica que la clase es una entidad.
 *
 * La anotacion @Table indica que la clase sera mapeada a una tabla y persistida
 *
 */

@Entity
@Table(name="CRAPSROLL")
public class CrapsRoll implements Serializable {

    @Id
    @Column(name = "ID_CRAPSROLL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCrapsRoll;

    @Column(name = "ID_PLAYER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlayer;

    @Column(name = "CRAP_ONE")
    private Short crapOne;

    @Column(name = "CRAP_TWO")
    private Short crapTwo;

    @Column(name = "ROLL_RESULT")
    private Short rollResult;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="ID_PLAYER", insertable = false, updatable = false)
    private Player player;

    @OneToOne(cascade=CascadeType.PERSIST,mappedBy = "crapsRoll")
    private Game game;


    public CrapsRoll() {
    }

    @PreRemove
    private void preRemove() {
        game.setCrapsRoll(null);

    }

    public Long getIdCrapsRoll() {
        return idCrapsRoll;
    }

    public void setIdCrapsRoll(Long idCrapsRoll) {
        this.idCrapsRoll = idCrapsRoll;
    }

    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Short getCrapOne() {
        return crapOne;
    }

    public void setCrapOne(Short crapOne) {
        this.crapOne = crapOne;
    }

    public Short getCrapTwo() {
        return crapTwo;
    }

    public void setCrapTwo(Short crapTwo) {
        this.crapTwo = crapTwo;
    }

    public Short getRollResult() {
        return rollResult;
    }

    public void setRollResult(Short rollResult) {
        this.rollResult = rollResult;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
