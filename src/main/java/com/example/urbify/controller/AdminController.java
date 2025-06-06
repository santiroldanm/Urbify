package com.example.urbify.controller;

import com.example.urbify.models.Admin;
import com.example.urbify.models.Multa;
import com.example.urbify.models.Pago;
import com.example.urbify.models.Salones;
import com.example.urbify.service.PagoService;
import com.example.urbify.service.SalonesService;
import com.example.urbify.models.Vigilant;
import com.example.urbify.service.AdminService;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.MultaService;
import com.example.urbify.service.VehicleService;
import com.example.urbify.service.VigilantService;
import com.example.urbify.service.VisitanteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private VigilantService vigilantService;

    @Autowired
    private VisitanteService visitanteService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private ApartamentosService apartamentosService;

    @Autowired
    private SalonesService salonesService;

    @Autowired
    private MultaService multaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Panel de acciones del administrador
    @GetMapping("/action")
    public String adminAction(Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        List<Vigilant> vigilantes = vigilantService.listAllVigilants();
        long conteoVigilantes = vigilantService.countActiveVigilants();
        long conteoVisitantes = visitanteService.countActiveVisitors();
        long conteoVehiculos = vehicleService.countActiveVehicle();
        long conteoAptos = apartamentosService.countAvailable();
        List<Pago> aptosMorosos = pagoService.getByEstado("MOROSO");

        model.addAttribute("admin", admin);
        model.addAttribute("vigilantes", vigilantes);
        model.addAttribute("conteoVigilantes", conteoVigilantes);
        model.addAttribute("conteoVisitantes", conteoVisitantes);
        model.addAttribute("conteoVehiculos", conteoVehiculos);
        model.addAttribute("conteoAptos", conteoAptos);
        model.addAttribute("aptosMorosos", aptosMorosos);
        return "admin-view/admin-action";
    }

    // Mostrar lista de vigilantes desde el administrador
    @GetMapping("/vigilants")
    public String showVigilantsList(Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        List<Vigilant> vigilantes = vigilantService.listAllVigilants();

        model.addAttribute("admin", admin);
        model.addAttribute("vigilantes", vigilantes);
        return "vigilant-view/vigilant-list";
    }

    // Formulario para crear un vigilante (desde el admin)
    @GetMapping("/vigilants/new")
    public String showNewVigilantForm(Model model, Principal principal) {
        Admin admin = adminService.findByEmail(principal.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("vigilant", new Vigilant());
        return "vigilant-view/vigilant-form";
    }

    // Guardar un nuevo vigilante
    @PostMapping("/vigilants")
    public String saveVigilant(@ModelAttribute Vigilant vigilant, Principal principal) {
        Admin admin = adminService.findByEmail(principal.getName());
        if (admin == null) {
            return "redirect:/admins/vigilants?error=admin_not_found";
        }

        vigilant.setAdmin(admin);
        vigilant.setPassword(passwordEncoder.encode(vigilant.getPassword()));
        vigilantService.save(vigilant);

        return "redirect:/admins/vigilants";
    }

    // Formulario para editar un vigilante (desde el admin)
    @GetMapping("/vigilants/edit/{id}")
    public String showFormEditVigilant(@PathVariable Long id, Model model, Principal principal) {
        Vigilant vigilant = vigilantService.getById(id);
        if (vigilant == null) {
            return "redirect:/admins/vigilants";
        }

        Admin admin = adminService.findByEmail(principal.getName());

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("admin", admin);

        return "vigilant-view/vigilant-form";
    }

    // Formulario para eliminar un vigilante (desde el admin)
    @PostMapping("/vigilants/delete/{id}")
    public String deleteVigilant(@PathVariable Long id) {
        Vigilant vigilant = vigilantService.getById(id);

        if (vigilant == null) {
            return "redirect:/admins/vigilants?error=vigilant_not_found";
        }

        try {
            vigilantService.delete(id);
        } catch (Exception e) {
            return "redirect:/admins/vigilants?error=delete_failed";
        }

        return "redirect:/admins/vigilants?success=deleted";
    }

    // Reportes

    @GetMapping("/reports")
    public String reports(Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        List<Vigilant> vigilantes = vigilantService.listAllVigilants();
        List<Pago> aptosMorosos = pagoService.getByEstado("MOROSO");
        List<Salones> reservas = salonesService.findAll();
        List<Multa> multas = multaService.findAll();

        model.addAttribute("admin", admin);
        model.addAttribute("vigilantes", vigilantes);
        model.addAttribute("aptosMorosos", aptosMorosos);
        model.addAttribute("reservas", reservas);
        model.addAttribute("multas", multas);
        return "admin-view/reports";
    }

}
