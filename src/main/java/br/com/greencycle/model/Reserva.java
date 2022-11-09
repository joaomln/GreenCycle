package br.com.greencycle.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer cdReserva;

    @FutureOrPresent(message = "{futOrPres}")
    @NotNull(message = "{notNull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dtRetirada;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "{futOrPres}")
    private LocalDate dtRetorno;

    @ManyToOne
    @NotNull(message = "{notNull}")
    private Usuario usuario;

    @ManyToOne
    @NotNull(message = "{notNull}")
    private Bike bike;

    @ManyToOne
    @NotNull(message = "{notNull}")
    private Empresa empresaRetirada;

    @ManyToOne
    private Empresa empresaDevolucao;

}
