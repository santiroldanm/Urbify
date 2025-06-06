package com.example.urbify.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.urbify.models.Parqueadero; 
import com.example.urbify.models.Vehicle;
import com.example.urbify.models.Vigilant;
import com.example.urbify.models.Visitante;
import com.example.urbify.service.ParqueaderoService; 
import com.example.urbify.service.VehicleService;
import com.example.urbify.service.VigilantService;

@Controller
@RequestMapping("/parqueadero")
public class ParqueaderoController {

    @Autowired
    private ParqueaderoHelper parqueaderoHelper;

    @Autowired
    private ParqueaderoService parqueaderoService;

    @Autowired
    private VigilantService vigilantService;
    @Autowired

    private VehicleService vehicleService;

    ParqueaderoController(ParqueaderoHelper parqueaderoHelper) {
        this.parqueaderoHelper = parqueaderoHelper;
    }

    // Crear nuevo parqueadero
    @GetMapping("/new")
    public String showCreateForm(@ModelAttribute Visitante visitante, Principal principal, Model model,
            RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        try {
            model.addAttribute("vigilant", vigilant); 
            model.addAttribute("parqueadero", new Parqueadero());
            model.addAttribute("vehiculos",
                    parqueaderoHelper.obtenerVehiculosDisponibles(vehicleService.listAllVehicles(),
                            parqueaderoService.findAll()));

            return "visitors-view/parqueadero-form";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el Parqueadero");
            return "redirect:/vigilant/vehicles";
        }
    }

    // Crear un nuevo parqueadero
    @SuppressWarnings("null")
    @PostMapping("/new")
    public String create(@Validated @ModelAttribute Parqueadero parqueadero, BindingResult result, Model model,
            @RequestParam(required = false) Integer valorExtra, Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            Vehicle vehiculoSeleccionado = vehicleService.findVehicleById(parqueadero.getVehicle().getId());
            if (vehiculoSeleccionado != null) {
                parqueadero.setTipo(vehiculoSeleccionado.getType()); 
            }
            boolean existente = parqueaderoService.existsByEspacioAndTipo(parqueadero.getEspacio(),
                    parqueadero.getTipo());

            if (existente) {
                model.addAttribute("error", "El espacio ya esta ocupado");
                model.addAttribute("vigilant", vigilantService.findByEmail(principal.getName())
                        .orElseThrow(() -> new RuntimeException("Vigilante no encontrado")));
                model.addAttribute("parqueadero", parqueadero);
                List<Vehicle> disponibles = parqueaderoHelper.obtenerVehiculosDisponibles(
                        vehicleService.listAllVehicles(),
                        parqueaderoService.findAll());
                model.addAttribute("vehiculos", disponibles);

                return "visitors-view/parqueadero-form";

            } else if (vehicleService.existsByPlate(parqueadero.getVehicle().getPlate())
                    && parqueaderoService.existsByVehicle(parqueadero.getVehicle())) {
                model.addAttribute("error", "Vehiculo ya registrado");
                model.addAttribute("vigilant", vigilantService.findByEmail(principal.getName())
                        .orElseThrow(() -> new RuntimeException("Vigilante no encontrado")));
                model.addAttribute("parqueadero", parqueadero);
                model.addAttribute("vehiculos", parqueaderoHelper
                        .obtenerVehiculosDisponibles(vehicleService.listAllVehicles(), parqueaderoService.findAll()));

                return "visitors-view/parqueadero-form";
            }

            parqueaderoService.save(parqueadero); 
            return "redirect:/parqueadero/list"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el vehículo");
            return "redirect:/vigilant/vehicles";
        }
    }

    // Mostrar el formulario de edición de parqueadero
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal,
            RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        try {
            Parqueadero parqueadero = parqueaderoService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Parqueadero no encontrado"));
            model.addAttribute("parqueadero", parqueadero);
            model.addAttribute("vigilant", vigilant);
            model.addAttribute("vehiculos",
                    parqueaderoHelper.obtenerVehiculosDisponibles(vehicleService.listAllVehicles(),
                            parqueaderoService.findAll()));

            return "visitors-view/parqueadero-form"; 

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el vehículo");
            return "redirect:/vigilant/vehicles";
        }
    }

    // Eliminar un parqueadero
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        parqueaderoService.delete(id); 
        return "redirect:/parqueadero/list"; 
    }

    // Mostrar la lista de parqueaderos
    @GetMapping("/list")
    public String listAll(Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        model.addAttribute("parqueaderos", parqueaderoService.findAll());
        model.addAttribute("vigilant", vigilant);
        return "visitors-view/parqueadero-list"; 
    }
}