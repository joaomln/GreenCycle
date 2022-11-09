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

    public void create(Reserva reserva) {
        reserva.setCdReserva(null);
        reserva.setDtRetorno(null);
        reserva.setEmpresaDevolucao(null);
        reserva.getBike().setEmpresa(null);

        reservaRepository.save(reserva);
    }

    public void finish(Reserva reserva, FimReservaBean fimReservaBean) {
        reserva.setEmpresaDevolucao(fimReservaBean.getEmpresaDevolucao());
        reserva.getBike().setEmpresa(reserva.getEmpresaDevolucao());

        reservaRepository.save(reserva);
    }

    public List<Reserva> findRunningReservas() {
        return reservaRepository.findRunningReservas();
    }

    public List<Reserva> findByBike(Bike bike) {
        return reservaRepository.findByBike(bike);
    }

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

    public boolean canFinish(Reserva reserva) {
        return reserva.getDtRetorno() == null && reserva.getEmpresaDevolucao() == null;
    }

    public boolean canCreate(Reserva reserva) {
        return bikeService.findByEmpresa(reserva.getEmpresaRetirada()).contains(reserva.getBike());
    }

    public boolean cleanDates(Reserva reserva, FimReservaBean fimReservaBean) {
        if (reserva.getDtRetirada().isAfter(fimReservaBean.getDtRetorno())) {
            return false;
        } else {
            reserva.setDtRetorno(fimReservaBean.getDtRetorno());
            return true;
        }
    }
}
