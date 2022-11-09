package br.com.greencycle.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Bike implements Serializable {

    @Id
    @NotBlank(message = "{notBlank}")
    @EqualsAndHashCode.Include
    private String idBike;

    @NotBlank(message = "{notBlank}")
    private String marca;

    @NotBlank(message = "{notBlank}")
    private String tipo;

    @NotBlank(message = "{notBlank}")
    private String modelo;

    @ManyToOne
    private Empresa empresa;

    public void setIdBike(String idBike) {
        this.idBike = idBike.strip();
    }

    public void setModelo(String modelo) {
        this.modelo = modelo.strip();
    }

    @Override
    public String toString() {
        return "(" + idBike + ") " + modelo;
    }
}
