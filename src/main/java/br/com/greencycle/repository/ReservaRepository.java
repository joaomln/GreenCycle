package br.com.greencycle.repository;

import br.com.greencycle.model.Bike;
import br.com.greencycle.model.Reserva;
import br.com.greencycle.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("SELECT r FROM Reserva r WHERE r.dtRetorno IS NULL AND r.empresaDevolucao IS NULL")
    List<Reserva> findRunningReservas();

    List<Reserva> findByBike(Bike bike);

    List<Reserva> findByUsuario(Usuario usuario);

}
