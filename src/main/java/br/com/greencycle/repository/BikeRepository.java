package br.com.greencycle.repository;

import br.com.greencycle.model.Bike;
import br.com.greencycle.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<Bike, String> {
    List<Bike> findByEmpresa(Empresa empresa);
}
