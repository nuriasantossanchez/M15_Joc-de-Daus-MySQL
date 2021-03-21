package jocDeDaus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

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
public class PlayerDto extends ResponseDto{

    private Long idPlayer;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
    private Date entryDate;
    private String name;
    private Double ranking;

    public PlayerDto() {
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


    @Override
    public String toString() {
        return "PlayerDto {" +
                "idPlayer=" + idPlayer +
                ", entryDate=" + entryDate +
                ", name='" + name + '\'' +
                ", successRanking=" + ranking +
                '}';
    }
}
