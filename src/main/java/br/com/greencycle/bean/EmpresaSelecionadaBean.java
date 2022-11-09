package br.com.greencycle.bean;

import br.com.greencycle.model.Empresa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EmpresaSelecionadaBean {
    @NotNull
    private Empresa empresa;
}
