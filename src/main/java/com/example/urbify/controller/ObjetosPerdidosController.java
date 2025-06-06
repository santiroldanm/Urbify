package com.example.urbify.controller;

import com.example.urbify.models.ObjetosPerdidos;
import com.example.urbify.models.ObjetosPerdidos.EstadoObjeto; // Importar el Enum
import com.example.urbify.models.Vigilant; // Asumo que tienes una entidad Vigilant
import com.example.urbify.service.ObjetosPerdidosService;
import com.example.urbify.service.VigilantService; // Si necesitas datos del vigilante logueado

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ObjetosPerdidos")
public class ObjetosPerdidosController {

    @Autowired
    private ObjetosPerdidosService objetosPerdidosService;
    @Autowired
    private VigilantService vigilantService;

    
    // Muestra la lista de objetos perdidos y el formulario para añadir uno nuevo
    @GetMapping("/list")
    public String showObjetosPerdidos(Model model, Principal principal,
                                      @ModelAttribute("successMessage") String successMessage,
                                      @ModelAttribute("errorMessage") String errorMessage) {
           Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        model.addAttribute("vigilant", vigilant);

        List<ObjetosPerdidos> objetosPerdidos = objetosPerdidosService.findAll();
        model.addAttribute("objetosPerdidos", objetosPerdidos);


        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("EstadoObjeto", EstadoObjeto.class);
        return "vigilant-view/objetos-perdidos"; 
    }

    // Guardar un nuevo objeto perdido
    @PostMapping("/save")
    public String saveObjetoPerdido(@RequestParam("descripcion") String descripcion,
                                   @RequestParam("ubicacionEncontrado") String ubicacionEncontrado,
                                   @RequestParam("fechaEncontrado") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaEncontrado,
                                   RedirectAttributes redirectAttributes) {
        try {
            ObjetosPerdidos nuevoObjeto = new ObjetosPerdidos();
            nuevoObjeto.setDescripcion(descripcion);
            nuevoObjeto.setUbicacionEncontrado(ubicacionEncontrado);
            nuevoObjeto.setFechaEncontrado(fechaEncontrado);
            nuevoObjeto.setEstado(EstadoObjeto.ENCONTRADO); 

            objetosPerdidosService.save(nuevoObjeto);
            redirectAttributes.addFlashAttribute("successMessage", "Objeto perdido registrado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar el objeto: " + e.getMessage());
            e.printStackTrace(); 
        }
        return "redirect:/ObjetosPerdidos/list";
    }

    // Eliminar un objeto perdido
    @PostMapping("/delete")
    public String deleteObjetoPerdido(@RequestParam("id") Long id,
                                      RedirectAttributes redirectAttributes) {
        try {
            objetosPerdidosService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Objeto perdido eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el objeto: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/ObjetosPerdidos/list";
    }

    // Método para marcar un objeto como reclamado/entregado
    @PostMapping("/markReclamado")
public String markObjetoReclamado(@RequestParam("id") Long id,
                                  @RequestParam(value = "nombreQuienReclama", required = false) String nombreQuienReclama,
                                  RedirectAttributes redirectAttributes) {
    try {
        objetosPerdidosService.markAsReclamado(id, nombreQuienReclama);
        redirectAttributes.addFlashAttribute("successMessage", "Objeto marcado como reclamado/entregado.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al marcar como reclamado: " + e.getMessage());
        e.printStackTrace();
    }
    return "redirect:/ObjetosPerdidos/list";
}
}