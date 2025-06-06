package com.example.urbify.controller;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.urbify.models.Admin;
import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Observaciones; 
import com.example.urbify.models.Observaciones.Tipo; 
import com.example.urbify.models.Vigilant;
import com.example.urbify.service.AdminService;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.ObservacionesService; 
import com.example.urbify.service.PropietarioService;
import com.example.urbify.service.VigilantService;
import com.example.urbify.service.VisitanteService;

@Controller
@RequestMapping("/Correspondencias")
public class CorrespondeciaController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private ApartamentosService apartamentosService;

    @Autowired
    private PropietarioService propietarioService;

    @Autowired
    private VigilantService vigilantService;

    @Autowired
    private VisitanteService visitanteService;

    @Autowired
    private ObservacionesService observacionesService; 

    @GetMapping("/list")
    public String showRentersList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer torre,
            @RequestParam(required = false) Integer piso,
            @RequestParam(required = false) String search,
            Model model,
            Principal principal) {

        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        Pageable pageable = PageRequest.of(page, 8);
        Page<Apartamentos> apartamentosPage;

        // Lógica de filtros
        if (search != null && !search.isEmpty()) {
            apartamentosPage = apartamentosService.searchByNombreOrApartamento(search, pageable);
        } else if (torre != null || piso != null) {
            apartamentosPage = apartamentosService.filterByTorreAndPiso(torre, piso, pageable);
        } else {
            apartamentosPage = apartamentosService.getPaginatedApartments(pageable);
        }

        int totalPages = apartamentosPage.getTotalPages();
        int currentPage = page;

        int startPage = Math.max(0, currentPage - 1);
        int endPage = Math.min(startPage + 2, totalPages - 1);

        // Ajustar si estamos al final
        if (endPage - startPage < 2) {
            startPage = Math.max(0, endPage - 2);
        }

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("visitantes", visitanteService.findAll());
        model.addAttribute("apartamentosPage", apartamentosPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("torre", torre);
        model.addAttribute("piso", piso);
        model.addAttribute("search", search);

        return "vigilant-view/correspondencias";
    }

    // Muestra el formulario para añadir una Observación (Paquete o Recibo)
    @GetMapping("/addObservacion")
    public String showAddObservacionForm(
            @RequestParam("apartamentoId") Long apartamentoId,
            @RequestParam("tipo") String tipoString, // "Encomienda" o "Recibo"
            Model model,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        try {
            Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
            model.addAttribute("vigilant", vigilant);

            Tipo tipo = Tipo.valueOf(tipoString); 

            Observaciones nuevaObservacion = observacionesService.createPredefinedObservacion(apartamentoId, tipo);

            model.addAttribute("observacion", nuevaObservacion);
            model.addAttribute("tiposObservacion", Tipo.values()); 
            return "vigilant-view/correspondencias"; 

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tipo de observación inválido: " + tipoString);
            return "redirect:/Correspondencias/list";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/Correspondencias/list";
        }
    }

    //Guardar la Observación
    @PostMapping("/saveObservacion")
    public String saveObservacion(
            @ModelAttribute("observacion") Observaciones observacion, 
            @RequestParam("apartamentoId") Long apartamentoId, 
            @RequestParam("correspondencia") String correspondencia, 
            @RequestParam("tipo") Tipo tipo, 
            RedirectAttributes redirectAttributes) {
        try {
            
            Apartamentos apartamento = apartamentosService.findById(apartamentoId);
            if (apartamento == null) {
                throw new RuntimeException("Apartamento no encontrado");
            }
            observacion.setApartamento(apartamento);
            observacion.setCorrespondencia(correspondencia); 
            observacion.setTipo(tipo); 
            observacion.setActive(true);
            observacion.setCreatedAt(new Date());

            observacionesService.saveObservacion(observacion);
            redirectAttributes.addFlashAttribute("successMessage", "Correspondencia registrada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al registrar la correspondencia: " + e.getMessage());
            e.printStackTrace(); 
        }
        return "redirect:/Correspondencias/list";
    }

    @GetMapping("/deleteObservacion")
    public String deleteObservacion(@RequestParam("id") Long observacionId, RedirectAttributes redirectAttributes) {
        try {
            observacionesService.deleteObservacion(observacionId);
            redirectAttributes.addFlashAttribute("successMessage", "Correspondencia eliminada con éxito.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la correspondencia: " + e.getMessage());
        }
        return "redirect:/Correspondencias/list";
    }
}