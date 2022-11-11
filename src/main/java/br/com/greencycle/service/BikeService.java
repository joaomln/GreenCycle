package br.com.greencycle.service;

import br.com.greencycle.model.Bike;
import br.com.greencycle.model.Empresa;
import br.com.greencycle.repository.BikeRepository;
import br.com.greencycle.util.MessagesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BikeService {

    @Autowired
    private MessagesBean messages;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ReservaService reservaService;

    public List<Bike> findByEmpresa(Empresa empresa) {
        return bikeRepository.findByEmpresa(empresa);
    }

    public Bike create(Bike bike) {
        if (bike.getEmpresa() == null) {
            throw new IllegalArgumentException(messages.get("bikeEnterpriseNotNull"));
        }
        if (bike.getEmpresa().getIdEmpresa() == null || !empresaService.existsById(bike.getEmpresa().getIdEmpresa())) {
            throw new EntityNotFoundException(messages.get("enterpriseNotFound"));
        }
        if (bikeRepository.existsById(bike.getIdBike())) {
            throw new EntityExistsException(messages.get("bikeAlreadyExists"));
        }
        return bikeRepository.save(bike);
    }

    public void deleteById(String idBike) {
        Bike bike = bikeRepository.findById(idBike)
                .orElseThrow(() -> new EntityNotFoundException(messages.get("bikeNotFound")));
        if (!canDelete(bike)) {
            throw new IllegalArgumentException(messages.get("bikeDeleteError"));
        }
        bikeRepository.delete(bike);
    }

    public boolean canDelete(Bike bike) {
        return bike.getEmpresa() != null && reservaService.findByBike(bike).isEmpty();
    }

}
