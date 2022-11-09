package br.com.greencycle.bean;

import br.com.greencycle.model.Empresa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * A class that is used as form backing bean to select all cars of a station.
 * This station selection is done via this bean.
 */
@Getter
@Setter
@NoArgsConstructor
public class EmpresaSelecionadaBean {
    @NotNull
    private Empresa empresa;
}
