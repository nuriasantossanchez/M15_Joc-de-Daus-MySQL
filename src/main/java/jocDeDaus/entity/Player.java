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
 * La anotacion @Table indica que la clase sera mapeada a una tabla y persistida, en este caso,
 * tanto en una base de datos embebida de tipo H2, como en una base de datos MySQL.
 * (ver application.properties, donde estan definidas ambas conexiones)
 *
 * La anotacion @IdClass especifica que la clase tiene clave primaria compuesta, es decir, se asigna a varios campos
 * o propiedades de la entidad.
 * Los nombres de los campos o propiedades en la clase de clave primaria, en este caso, GamePlayerPk.class,
 * y los campos o propiedades de la clave primaria de la entidad se deben corresponder y sus tipos deben ser los mismos.
 *
 * La anotacion @ManyToOne especifica una asociacion de un solo valor a otra clase de entidad
 * que tiene multiplicidad de muchos a uno, en este caso, la asociacion es con la entidad Game,
 * donde varios valores de tipo Player pueden estar asociados a una unica entidad de tipo Game.
 *
 * La anotacion @JoinColumn indica que la propiedad game es el campo para crear la relacion de llave foranea
 * y va a tomar la columna id_shop de la tabla PICTURE para crear el join con la tabla padre SHOP
 *
 * La anotacion @PrePersist especifica un metodo de devolucion de llamada para el evento de ciclo de vida
 * correspondiente, en este caso, define ciertos valores por defecto previos a la insercion en base de datos.
 *
 * La anotacion @Temporal debe especificarse para campos persistentes o propiedades de tipo java.util.Date
 * y java.util.Calendar. Solo se puede especificar para campos o propiedades de este tipo.
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

    @Override
    public String toString() {
        return "\nPlayer{" +
                "idPlayer=" + idPlayer +
                ", entryDate=" + entryDate +
                ", name='" + name + '\'' +
                ", ranking=" + ranking +
                ", crapsRolls=" + crapsRolls +
                ", games=" + games +
                '}';
    }
}
