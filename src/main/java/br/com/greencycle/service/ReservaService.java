package br.com.greencycle.service;

import br.com.greencycle.bean.FimReservaBean;
import br.com.greencycle.model.Bike;
import br.com.greencycle.model.Reserva;
import br.com.greencycle.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private BikeService bikeService;

    /**
     * Saves the given rental.
     * Sets following fields to null: id, km, return date, return station und the
     * station of the car
     *
     * @param reserva the rental to be saved
     */
    public void create(Reserva reserva) {
        reserva.setCdReserva(null);
        reserva.setDtRetorno(null);
        reserva.setEmpresaDevolucao(null);
        reserva.getBike().setEmpresa(null);

        reservaRepository.save(reserva);
    }

    /**
     * Finishes the given rental with the help of a {@link FimReservaBean}.
     * Updates the record afterwards.
     *
     * @param reserva        the rental to be updated
     * @param fimReservaBean the bean
     */
    public void finish(Reserva reserva, FimReservaBean fimReservaBean) {
        reserva.setEmpresaDevolucao(fimReservaBean.getEmpresaDevolucao());
        reserva.getBike().setEmpresa(reserva.getEmpresaDevolucao());

        reservaRepository.save(reserva);
    }

    /**
     * Returns a list of all running rentals.<br>
     * See {@link ReservaRepository#findRunningReservas()}
     */
    public List<Reserva> findRunningReservas() {
        return reservaRepository.findRunningReservas();
    }

    /**
     * Returns a list of all rentals that have a specific car.<br>
     * See {@link ReservaRepository#findByBike(Bike)}
     */
    public List<Reserva> findByBike(Bike bike) {
        return reservaRepository.findByBike(bike);
    }

    /**
     * Checks whether a rental with a given id exists and can be finished.
     * If the id is null or the rental was not found, an empty {@link Optional} is
     * returned.
     * If the rental was found and can be finished (see
     * {@link #canFinish(Reserva)}),
     * then an {@link Optional} with the found rental is returned.
     */
    public Optional<Reserva> existsAndCanFinish(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<Reserva> opt = reservaRepository.findById(id);
        Reserva reserva;
        if (opt.isPresent() && canFinish((reserva = opt.get()))) {
            return Optional.of(reserva);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Checks whether a given rental can be finished or not.
     * Returns true if return date, km and return station fields are null, and false
     * otherwise.
     */
    public boolean canFinish(Reserva reserva) {
        return reserva.getDtRetorno() == null && reserva.getEmpresaDevolucao() == null;
    }

    /**
     * Checks whether a rental can be created ot not. A rental can be created, if
     * the provided car is indeed a car of the provided station.
     * This method is used for security reasons, since users can manipulate the
     * frontend values via F12.
     */
    public boolean canCreate(Reserva reserva) {
        return bikeService.findByEmpresa(reserva.getEmpresaRetirada()).contains(reserva.getBike());
    }

    /**
     * Returns false if the rental date of rental is after the return date of
     * finishRentalBean.
     * If that is not the case, then the return date of rental will be set to the
     * return date of finishRentalBean and true will be returned.
     */
    public boolean cleanDates(Reserva reserva, FimReservaBean fimReservaBean) {
        if (reserva.getDtRetirada().isAfter(fimReservaBean.getDtRetorno())) {
            return false;
        } else {
            reserva.setDtRetorno(fimReservaBean.getDtRetorno());
            return true;
        }
    }
}
