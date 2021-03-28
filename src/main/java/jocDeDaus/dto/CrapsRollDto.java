package jocDeDaus.dto;

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
public class CrapsRollDto extends ResponseDto{

    private Long idCrapsRoll;
    private Long idPlayer;
    private Short crapOne;
    private Short crapTwo;
    private Short rollResult;

    public CrapsRollDto() {
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

    @Override
    public String toString() {
        return "CrapsRollDto {" +
                "idCrapsRoll=" + idCrapsRoll +
                ", idPlayer=" + idPlayer +
                ", crapOne=" + crapOne +
                ", crapTwo=" + crapTwo +
                ", rollResult=" + rollResult +
                '}';
    }
}
