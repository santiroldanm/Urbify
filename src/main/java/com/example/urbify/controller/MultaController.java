package com.example.urbify.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.urbify.models.Admin;
import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Multa;
import com.example.urbify.models.Salones;
import com.example.urbify.repository.MultaRepository;
import com.example.urbify.service.AdminService;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.MultaService;

@Controller
@RequestMapping("/multas")
public class MultaController {

    @Autowired
    private MultaService multaService;

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ApartamentosService apartamentosService;

    //Registrar nueva multa
    @GetMapping("/new")
    public String nuevaMulta(Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        Multa multa = new Multa();

        model.addAttribute("multa", multa);
        model.addAttribute("admin", admin);
        model.addAttribute("apartamentos", apartamentosService.listActive());

        return "admin-view/multas-form";
    }

    //Guardar una multa
    @PostMapping("/save")
    public String guardarMulta(@ModelAttribute Multa multa) {
        multaService.save(multa);
        return "redirect:/admins/reports";
    }

    // Editar multa
    @GetMapping("/editar/{id}")
    public String editarMulta(@PathVariable Long id, Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("multa", multa);
        model.addAttribute("apartamentos", apartamentosService.listActive());
        model.addAttribute("admin", admin);
        return "admin-view/multas-form";
    }

    // Eliminar multa
    @PostMapping("/delete/{id}")
    public String eliminarMulta(@PathVariable Long id) {
        multaRepository.deleteById(id);
        return "redirect:/admins/reports";
    }
}
