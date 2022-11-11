package br.com.greencycle.controller.rest;

import br.com.greencycle.bean.FimReservaBean;
import br.com.greencycle.model.Bike;
import br.com.greencycle.model.Reserva;
import br.com.greencycle.model.Empresa;
import br.com.greencycle.service.BikeService;
import br.com.greencycle.service.UsuarioService;
import br.com.greencycle.service.ReservaService;
import br.com.greencycle.service.EmpresaService;
import br.com.greencycle.util.MessagesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
@SessionAttributes({ "fimReservaBean" })
public class AdminController {

    @Autowired
    private MessagesBean messages;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/criar-reserva")
    public String showCreateRentalForm(Model model, @ModelAttribute("reserva") Reserva reserva) {
        List<Empresa> empresas = empresaService.findAll();
        List<Bike> bikes;

        if (empresas.isEmpty()) {
            bikes = new ArrayList<>();
        } else {
            bikes = bikeService
                    .findByEmpresa(
                            reserva.getEmpresaRetirada() == null ? empresas.get(0) : reserva.getEmpresaRetirada());
        }

        reserva.setDtRetirada(LocalDate.now());
        model.addAttribute("reserva", reserva);
        model.addAttribute("bikes", bikes);
        model.addAttribute("empresas", empresas);
        model.addAttribute("usuarios", usuarioService.findAll());

        return "fragments/criar-reserva";
    }

    @PostMapping("/criar-reserva/refresh")
    public String refreshCreateRentalForm(@ModelAttribute("reserva") Reserva reserva,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("reserva", reserva);
        return "redirect:/admin/criar-reserva";
    }

    @PostMapping("/criar-reserva/process")
    public ModelAndView processCreateRentalForm(@Valid @ModelAttribute("reserva") Reserva reserva,
            BindingResult bindingResult) {
        ModelAndView createRentalForm = new ModelAndView("fragments/criar-reserva");
        List<Empresa> empresas = empresaService.findAll();
        createRentalForm.addObject("bikes", bikeService.findByEmpresa(empresas.get(0)));
        createRentalForm.addObject("empresas", empresas);
        createRentalForm.addObject("usuarios", usuarioService.findAll());

        if (bindingResult.hasErrors()) {
            return createRentalForm;
        }

        if (!reservaService.canCreate(reserva)) {
            return createRentalForm.addObject("bikeMismatchError", messages.get("bikeMismatchError"));
        }

        reservaService.create(reserva);
        return new ModelAndView("redirect:/admin/reservas-atuais")
                .addObject("success", messages.get("createRentalSuccess"));
    }

    @GetMapping("/reservas-atuais")
    public String showRunningRentalsForm(Model model, @ModelAttribute("error") String error,
            @ModelAttribute("success") String success) {
        model.addAttribute("reservas", reservaService.findRunningReservas());
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "fragments/reservas-atuais";
    }

    @GetMapping("/finalizar-reservas/{id}")
    public ModelAndView showFinishForm(@PathVariable("id") Integer id) {
        Optional<Reserva> opt = reservaService.existsAndCanFinish(id);
        if (opt.isEmpty()) {
            return new ModelAndView("redirect:/admin/reservas-atuais")
                    .addObject("error", messages.get("rentalNotFound"));
        }

        return new ModelAndView("fragments/finalizar-reservas")
                .addObject("empresas", empresaService.findAll())
                .addObject("fimReservaBean", FimReservaBean.fromReserva(opt.get()));
    }

    @PostMapping("/finalizar-reservas/{id}")
    public ModelAndView processFinishForm(@PathVariable("id") Integer id, @Valid FimReservaBean fimReservaBean,
            BindingResult bindingResult) {
        ModelAndView redirectRunningRentals = new ModelAndView("redirect:/admin/reservas-atuais");
        ModelAndView finishRentalForm = new ModelAndView("/fragments/finalizar-reservas");

        Optional<Reserva> opt = reservaService.existsAndCanFinish(id);
        if (opt.isEmpty()) {
            return redirectRunningRentals.addObject("error", messages.get("rentalNotFound"));
        }

        Reserva reserva = opt.get();
        finishRentalForm.addObject("empresas", empresaService.findAll());

        if (bindingResult.hasErrors()) {
            return finishRentalForm;
        }

        if (!reservaService.cleanDates(reserva, fimReservaBean)) {
            return finishRentalForm.addObject("returnDateError", messages.get("returnDateError"));
        }

        reservaService.finish(reserva, fimReservaBean);
        return redirectRunningRentals.addObject("success", messages.get("finishRentalSuccess"));
    }

}
