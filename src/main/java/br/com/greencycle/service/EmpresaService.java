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

    /**
     * @return a list of all stations
     */
    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    /**
     * Checks whether a station with the given id exists or not.
     *
     * @param idEmpresa the id to be searched for
     * @return true, if the station exists, false otherwise
     */
    public boolean existsById(Integer idEmpresa) {
        return empresaRepository.existsById(idEmpresa);
    }

}
