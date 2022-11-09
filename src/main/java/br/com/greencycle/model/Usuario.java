package br.com.greencycle.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Usuario implements Serializable {

    @Id
    @Range(min = 111_111, max = 999_999, message = "{customerNumberRange}")
    @EqualsAndHashCode.Include
    @NotNull(message = "{notNull}")
    private Integer idUsuario;

    @Size(min = 1, max = 255, message = "{nameRange}")
    @NotNull(message = "{notNull}")
    private String sobrenome;

    @Size(min = 1, max = 255, message = "{nameRange}")
    @NotNull(message = "{notNull}")
    private String nome;

    public void setLastName(String sobrenome) {
        this.sobrenome = sobrenome.strip();
    }

    public void setnome(String nome) {
        this.nome = nome.strip();
    }

    @Override
    public String toString() {
        return "(" + idUsuario + ") " + nome + ' ' + sobrenome;
    }
}
