package com.example.urbify.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.urbify.models.Admin;
import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Pago;
import com.example.urbify.models.Propietario;
import com.example.urbify.repository.PagoRepository;
import com.example.urbify.service.AdminService;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.PropietarioService;

@Controller
@RequestMapping("/renters")
public class RenterController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ApartamentosService apartamentosService;

    @Autowired
    private PropietarioService propietarioService;

    // Para mostrar los apartamentos
    @GetMapping("/list")
    public String showRentersList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer torre,
            @RequestParam(required = false) Integer piso,
            @RequestParam(required = false) String search,
            Model model,
            Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String email = principal.getName();
            Admin admin = adminService.findByEmail(email);

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

            model.addAttribute("admin", admin);
            model.addAttribute("apartamentosPage", apartamentosPage);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("torre", torre);
            model.addAttribute("piso", piso);
            model.addAttribute("search", search);

            return "admin-view/renter";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al mostar los inquilinos");
            return "redirect:/admin-view/renter";

        }

    }

    // Mostrar Formulario nuevo inquilino
    @GetMapping("/new")
    public String showNewVigilantForm(Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);
        List<Apartamentos> aptoAvailable = apartamentosService.getAvailable();

        model.addAttribute("propietario", new Propietario());
        model.addAttribute("admin", admin);
        model.addAttribute("aptoAvailable", aptoAvailable);
        return "admin-view/renters-form";
    }

    // Guardar nuevo inquilino
    @PostMapping("/new")
    public String saveOwner(@ModelAttribute Propietario propietario, BindingResult result,
            @RequestParam("apartamentoId") Long apartamentoId,
            Model model, Principal principal) {
        Admin admin = adminService.findByEmail(principal.getName());
        List<Apartamentos> aptoAvailable = apartamentosService.getAvailable();

        if (admin == null) {
            return "redirect:/admins/renters?error=admin_not_found";
        }

        boolean isNew = (propietario.getId() == null);

        
        if (isNew) {
            if (propietarioService.existsByIdentification(propietario.getIdentification())) {
                result.rejectValue("identification", "error.identification", "La identificación ya está registrada.");
            }
        } else {
            Propietario propietarioExistente = propietarioService.findByIdentification(propietario.getIdentification());
            if (propietarioExistente != null && !propietarioExistente.getId().equals(propietario.getId())) {
                result.rejectValue("identification", "error.identification",
                        "La identificación ya está registrada por otro propietario.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("propietario", propietario);
            model.addAttribute("admin", admin);
            model.addAttribute("apartamentoId", apartamentoId);
            model.addAttribute("aptoAvailable", aptoAvailable);
            return "admin-view/renters-form";
        }

        Apartamentos apartamento = apartamentosService.findById(apartamentoId);
        propietario.setAdmin(admin);
        propietario.setApartamento(apartamento);
        apartamento.setPropietario(propietario);

        propietarioService.save(propietario);
        apartamentosService.save(apartamento);

        if (isNew) {
            Pago primerPago = new Pago();
            primerPago.setApartamento(apartamento);
            primerPago.setMes("Junio");
            primerPago.setValor(550000.0);
            primerPago.setEstado("PAGADO");
            primerPago.setFechaPago(new Date());
            primerPago.setMetodoPago("Efectivo");
            primerPago.setCreatedAt(new Date());
            primerPago.setUpdatedAt(new Date());

            pagoRepository.save(primerPago);
        }

        return "redirect:/renters/list";
    }

    @GetMapping("renters/edit/{id}")
    public String showFormEditRenter(@PathVariable Long id, Model model, Principal principal) {

        Admin admin = adminService.findByEmail(principal.getName());
        Propietario propietario = propietarioService.findById(id);
        List<Apartamentos> aptoAvailable = apartamentosService.getAvailable();

        if (propietario == null) {
            return "redirect:renters/list";
        }

        model.addAttribute("propietario", propietario);
        model.addAttribute("aptoAvailable", aptoAvailable);
        model.addAttribute("admin", admin);
        return "admin-view/renters-form";
    }

    @PostMapping("/delete/{id}")
    public String deleteRenter(@PathVariable Long id, Model model) {
        Propietario propietario = propietarioService.findById(id);
        if (propietario == null) {
            return "redirect:/renters/list?error=propietario_not_found";
        }

        try {

            Apartamentos apartamento = propietario.getApartamento();


            List<Pago> pagos = pagoRepository.findByApartamento(apartamento);
            pagoRepository.deleteAll(pagos);


            propietarioService.delete(id);


            apartamento.setPropietario(null);
            apartamentosService.save(apartamento);

        } catch (Exception e) {
            return "redirect:/renters/list?error=delete_failed";
        }

        return "redirect:/renters/list?success=deleted";
    }

}