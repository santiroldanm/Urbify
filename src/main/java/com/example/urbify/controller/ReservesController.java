package com.example.urbify.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.urbify.models.Admin;
import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Propietario;
import com.example.urbify.models.Salones;
import com.example.urbify.service.AdminService;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.SalonesService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/reserves")
public class ReservesController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private SalonesService salonesService;

    @Autowired
    private ApartamentosService apartamentosService;

    // Listar todas las reservas (Salones)
    @GetMapping("/list")
    public String reservesAction(Principal principal, Model model) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);

        model.addAttribute("admin", admin);
        model.addAttribute("reservas", salonesService.findAll());
        return "admin-view/reserves"; 
    }

    // Mostrar formulario para nueva reserva
    @GetMapping("/new")
    public String nuevaReserva(@RequestParam String tipo,
            @RequestParam Long apartamentoId,
            Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        Salones reserva = new Salones();
        reserva.setTipoEspacio(tipo);

        Apartamentos apartamento = apartamentosService.findById(apartamentoId);
        reserva.setApartamento(apartamento);

        model.addAttribute("reserva", reserva);
        model.addAttribute("admin", admin);
        model.addAttribute("apartamentos", apartamentosService.listActive());

        return "admin-view/reserve-form"; 
    }

    // Guardar o actualizar reserva
    @PostMapping("/save")
public String saveReserva(@ModelAttribute("reserva") Salones reserva, Model model, Principal principal) {
    if (reserva.getApartamento() != null && reserva.getApartamento().getId() != null) {
        Apartamentos apt = apartamentosService.findById(reserva.getApartamento().getId());
        reserva.setApartamento(apt);
    }

    // Verificar si ya existe una reserva que se cruce (excepto si estamos editando la misma)
    List<Salones> reservasExistentes = salonesService.findByTipoEspacio(reserva.getTipoEspacio());
    for (Salones existente : reservasExistentes) {
        if (!existente.getId().equals(reserva.getId())
                && existente.getFechaHoraInicio().isBefore(reserva.getFechaHoraFin())
                && existente.getFechaHoraFin().isAfter(reserva.getFechaHoraInicio())) {

            Admin admin = adminService.findByEmail(principal.getName());
            model.addAttribute("admin", admin);
            model.addAttribute("reserva", reserva);
            model.addAttribute("apartamentos", apartamentosService.listActive());
            model.addAttribute("error", "Ya existe una reserva para este horario");

            return "admin-view/reserve-form";
        }
    }

    salonesService.save(reserva);
    return "redirect:/reserves/list";
}

    // Editar una reserva
    @GetMapping("/edit/{id}")
    public String showFormEditReserves(@PathVariable Long id, Model model, Principal principal) {
        Admin admin = adminService.findByEmail(principal.getName());
        Salones reserva = salonesService.findById(id);
        List<Apartamentos> aptoAvailable = apartamentosService.listActive();

        model.addAttribute("admin", admin);
        model.addAttribute("reserva", reserva); 
        model.addAttribute("apartamentos", aptoAvailable);
        return "admin-view/reserve-form"; 
    }

    //Eliminar una reserva
    @PostMapping("/delete")
    public String deleteReserva(@RequestParam("id") Long id) {
        salonesService.deleteById(id);
        return "redirect:/reserves/list";
    }
}
