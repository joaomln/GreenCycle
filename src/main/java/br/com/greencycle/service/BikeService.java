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

    /**
     * @param empresa the station to be searched for
     * @return all cars that are part of the given station
     */
    public List<Bike> findByEmpresa(Empresa empresa) {
        return bikeRepository.findByEmpresa(empresa);
    }

    /**
     * Saves the given car.
     *
     * @param bike car to be saved
     * @return the saved car coming from the database (only when no exceptions
     *         occur)
     * @throws EntityNotFoundException  if the provided station of the car does not
     *                                  exist
     * @throws EntityExistsException    if the given primary key already belongs to
     *                                  an existing entity
     * @throws IllegalArgumentException if the station is null
     */
    public Bike create(Bike bike) {
        if (bike.getEmpresa() == null) {
            throw new IllegalArgumentException(messages.get("carStationNotNull"));
        }
        if (bike.getEmpresa().getIdEmpresa() == null || !empresaService.existsById(bike.getEmpresa().getIdEmpresa())) {
            throw new EntityNotFoundException(messages.get("stationNotFound"));
        }
        if (bikeRepository.existsById(bike.getIdBike())) {
            throw new EntityExistsException(messages.get("carAlreadyExists"));
        }
        return bikeRepository.save(bike);
    }

    /**
     * Deletes a car with the given primary key.
     * However, the car must be deletable. See {@link #canDelete(Bike)}
     *
     * @param idBike the primary key to be searched for
     * @throws EntityNotFoundException  if the given primary key does not belong to
     *                                  any existing entity
     * @throws IllegalArgumentException if the car cannot be deleted
     */
    public void deleteById(String idBike) {
        Bike bike = bikeRepository.findById(idBike)
                .orElseThrow(() -> new EntityNotFoundException(messages.get("carNotFound")));
        if (!canDelete(bike)) {
            throw new IllegalArgumentException(messages.get("carDeleteError"));
        }
        bikeRepository.delete(bike);
    }

    /**
     * Checks whether a given car can be deleted or not.
     * The car can be deleted, if, and only if, the station car is not in use
     * (station equals null) and the car was never used in any rental before.
     *
     * @param bike the car to be checked
     * @return true, if the car fulfills the mentioned criteria, false otherwise.
     */
    public boolean canDelete(Bike bike) {
        return bike.getEmpresa() != null && reservaService.findByBike(bike).isEmpty();
    }

}
