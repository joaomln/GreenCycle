package br.com.greencycle.service;

import br.com.greencycle.model.Empresa;
import br.com.greencycle.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public boolean existsById(Integer idEmpresa) {
        return empresaRepository.existsById(idEmpresa);
    }

}
