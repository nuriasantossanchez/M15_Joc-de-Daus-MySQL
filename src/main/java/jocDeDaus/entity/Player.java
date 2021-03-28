package jocDeDaus.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Clase de la capa de dominio.
 *
 * La anotacion @Entity indica que la clase es una entidad.
 *
 * La anotacion @Table indica que la clase sera mapeada a una tabla y persistida
 *
 */

@Entity
@Table(name="PLAYER") //en caso que la tabla sea diferente
public class Player implements Serializable {

    //Atributos de entidad Player
    @Id
    @Column(name = "ID_PLAYER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlayer;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "ENTRY_DATE")
    private Date entryDate;

    @Column(name = "NAME")//no hace falta si se llama igual
    @NotNull(message = "name is required")
    private String name;

    @Transient
    private Double ranking;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.LAZY)
    private List<CrapsRoll> crapsRolls;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.LAZY)
    private List<Game> games;

    public Player() {
    }

    @PrePersist
    public void preInsert() {
        if (this.entryDate == null) {
            LocalDateTime localDateTime = LocalDateTime.now();
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            this.entryDate = date;
        }

        this.name=name.trim();
        if (this.name.isBlank()) {
            this.name = "ANONYMOUS";
        }
    }

    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRanking() {
        return ranking;
    }

    public void setRanking(Double ranking) {
        this.ranking = ranking;
    }

    public List<CrapsRoll> getCrapsRolls() {
        return crapsRolls;
    }

    public void setCrapsRolls(List<CrapsRoll> crapsRolls) {
        this.crapsRolls = crapsRolls;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
