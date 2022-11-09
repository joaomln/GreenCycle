package br.com.greencycle.controller.view;

import br.com.greencycle.bean.EmpresaSelecionadaBean;
import br.com.greencycle.service.BikeService;
import br.com.greencycle.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PublicController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("empresaSelecionadaBean", new EmpresaSelecionadaBean());
        model.addAttribute("empresas", empresaService.findAll());
        return "fragments/procurar-bikes";
    }

    @PostMapping
    public String processForm(Model model,
            @Valid @ModelAttribute("empresaSelecionadaBean") EmpresaSelecionadaBean empresaSelecionadaBean,
            BindingResult bindingResult) {
        model.addAttribute("empresas", empresaService.findAll());
        model.addAttribute("bikes",
                bindingResult.hasErrors() ? null : bikeService.findByEmpresa(empresaSelecionadaBean.getEmpresa()));
        return "fragments/procurar-bikes";
    }
}
