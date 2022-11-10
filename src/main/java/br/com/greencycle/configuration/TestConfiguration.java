package br.com.greencycle.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.greencycle.model.Bike;
import br.com.greencycle.model.Empresa;
import br.com.greencycle.model.Usuario;
import br.com.greencycle.repository.BikeRepository;
import br.com.greencycle.repository.EmpresaRepository;
import br.com.greencycle.repository.UsuarioRepository;

@Configuration
public class TestConfiguration implements CommandLineRunner {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    BikeRepository bikeRepository;

    @Override
    public void run(String... args) throws Exception {

        Empresa empresa1 = new Empresa(1, "Condominio Viver");
        Empresa empresa2 = new Empresa(2, "Condominio Boa Vista");
        Empresa empresa3 = new Empresa(3, "Condominio Hotel Suisse");
        empresaRepository.saveAll(List.of(empresa1, empresa2, empresa3));

        bikeRepository.save(
                new Bike("bike1", "Caloi", "Padrao", "BMX", empresa1));
        bikeRepository.save(
                new Bike("bike2", "BTWIN", "Padrao", "SPEED", empresa2));
        bikeRepository.save(
                new Bike("bike3", "Caloi", "Padrao", "SPEED", empresa3));
        bikeRepository.save(
                new Bike("bike4", "MONARK", "Padrao", "CITY", empresa2));
        bikeRepository.save(
                new Bike("bike5", "Multi", "Elétrica", "CITY", empresa3));
        bikeRepository.save(
                new Bike("bike6", "TREK", "Padrao", "BMX", empresa1));

        usuarioRepository.save(
                new Usuario(111111, "Jose Roberto", "jose@email.com"));
        usuarioRepository.save(
                new Usuario(222222, "Miguel Tito", "Tito@email.com"));
        usuarioRepository.save(
                new Usuario(333333, "Alfredo Santos", "Alfredo@email.com"));

    }
}