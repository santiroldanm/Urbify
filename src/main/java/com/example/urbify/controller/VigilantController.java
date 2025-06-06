package com.example.urbify.controller;

import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Parqueadero;
import com.example.urbify.models.ReporteVisitanteDTO;
import com.example.urbify.models.ReservaCancha;
import com.example.urbify.models.ReservaCancha;
import com.example.urbify.models.Vehicle;
import com.example.urbify.models.Visitante;
import com.example.urbify.models.Vigilant;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.ParqueaderoService;
import com.example.urbify.service.ReporteVisitanteService;
import com.example.urbify.service.Reserva_CanchaService;
import com.example.urbify.service.VehicleService;
import com.example.urbify.service.VigilantService;
import com.example.urbify.service.VisitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vigilant")
public class VigilantController {

    @Autowired
    private VigilantService vigilantService;
    @Autowired
    private ReporteVisitanteService reporteVisitanteService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ParqueaderoService parqueaderoService;

    @Autowired
    private VisitanteService VisitanteService;

    @Autowired
    private Reserva_CanchaService reservaService;

    @Autowired
    private ApartamentosService apartamentosService;

    // Panel de acciones del vigilante
    @GetMapping("/action")
    public String vigilantAction(Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        model.addAttribute("vigilant", vigilant);
        return "vigilant-view/vigilant-action";
    }

    // Listar vehículos
    @GetMapping("/vehicles")
    public String showVehiclesList(Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        List<Vehicle> vehicles = vehicleService.listAllVehicles();

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("vehicles", vehicles);

        return "visitors-view/vehicle-list";
    }

    // Mostrar formulario de nuevo vehículo
    @GetMapping("/vehicles/new")
    public String showNewVehicleForm(Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("apartamentos", apartamentosService.listAllApartments());

        return "visitors-view/vehicle-form";
    }

    // Guardar nuevo vehículo
    @PostMapping("/vehicles")
    public String saveVehicle(
            @RequestParam(name = "createdAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S") Date fecha,
            @ModelAttribute Vehicle vehicle, Principal principal,
            Model model, RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        try {
            if (fecha == null) {
                fecha = new Date();
            }

            // Verificar si la placa ya existe
            if (vehicleService.existsByPlate(vehicle.getPlate()) && !vehicleService.existsByid(vehicle.getId())) {
                model.addAttribute("error", "El mismo vehiculo esta adentro");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("vehicle", vehicle);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/vehicle-form";
            } else if (!vehicle.getApartamento().isActive()) {
                model.addAttribute("error", "Apartamento inactivo");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("vehicle", vehicle);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/vehicle-form";
            } else if (vehicleService.existsByid(vehicle.getId()) && vehicleService.existsByPlate(vehicle.getPlate())) {
                model.addAttribute("error", "El vehiculo ya ha ingresado anteriormente");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("vehicle", vehicle);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                Vehicle actual = vehicleService.findVehicleById(vehicle.getId());
                vehicle.setContador(1 + actual.getContador());

                vehicle.setCreatedAt(fecha);
                vehicle.setVigilant(vigilant);
                if (vehicle.isActive()) {
                    vehicle.setContador(1 + actual.getContador());
                    
                } else {
                    vehicle.setContador(actual.getContador());
                }
                vehicleService.save(vehicle);
                model.addAttribute("vehicles", vehicleService.listAllVehicles());

                return "visitors-view/vehicle-list";
            } else if (!apartamentosService.existsByApartamento(vehicle.getApartamento().getApartamento())) {
                model.addAttribute("error", "Apartamento no valido");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("vehicle", vehicle);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/vehicle-form";
            }

            vehicle.setVigilant(vigilant);
            vehicle.setCreatedAt(fecha);
            if (vehicle.isActive()) {
                vehicle.setContador(1);
            }
            vehicle.setUpdatedAt(new Date());
            vehicleService.save(vehicle);

            return "redirect:/vigilant/vehicles?success=Vehículo registrado exitosamente";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el vehículo");
            return "redirect:/vigilant/vehicles";
        }
    }

    // Mostrar formulario de edición
    @GetMapping("/vehicles/edit/{id}")
    public String showEditVehicleForm(@PathVariable Long id, Model model, Principal principal) {
        Vehicle vehicle = vehicleService.getById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("apartamentos", apartamentosService.listAllApartments());

        return "visitors-view/vehicle-form";
    }

    // Eliminar vehículo
    @PostMapping("/vehicles/delete/{id}")
    public String deleteVehicle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Vehículo eliminado exitosamente");
            return "redirect:/vigilant/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el vehículo");
            return "redirect:/vigilant/vehicles";
        }
    }

    // Mostrar parqueadero
    @GetMapping("/parqueadero")
    public String showParqueadero(@RequestParam(defaultValue = "1") int piso, Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        List<Parqueadero> parqueaderos = parqueaderoService.findAll();
        int inicio = (piso - 1) * 25 + 1;
        int fin = piso * 25;

        model.addAttribute("piso", piso);
        model.addAttribute("pisoInicio", inicio);
        model.addAttribute("pisoFin", fin);
        model.addAttribute("vigilant", vigilant);
        model.addAttribute("parqueadero", parqueaderos != null ? parqueaderos : Collections.emptyList());

        return "visitors-view/parqueadero";
    }

    // Guardar un nuevo visitante
    @PostMapping("/visitante")
    public String saveVisitante(@ModelAttribute Visitante visitante, Principal principal,
            Model model, RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        
        try {
            if (!apartamentosService.existsByApartamento(visitante.getApartamento().getApartamento())) {
                model.addAttribute("error", "Apartamento no valido");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("visitantes", visitante);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/visitant";
            }
            if (!visitante.getApartamento().isActive()) {
                model.addAttribute("error", "Apartamento inactivo pillín");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("visitantes", visitante);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/visitant";
            }

            visitante.setRegistradoPor(vigilant);
            visitante.setFechaIngreso(new Date());
            visitante.setActive(true);
            VisitanteService.save(visitante);

            return "redirect:/vigilant/visitante?success=Visitante registrado exitosamente";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el visitante");
            return "redirect:/vigilant/visitante";
        }
    }

    @PostMapping("/visitantes/delete/{id}")
    public String deleteVisitante(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            VisitanteService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Visitante eliminado exitosamente");
            return "redirect:/vigilant/visitante";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar visitante");
            return "redirect:/vigilant/visitante";
        }
    }

    // Mostrar visitantantes
    @GetMapping("/visitante")
    public String showViistante(Model model, Principal principal) {

        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        List<Visitante> visitantes = VisitanteService.findAll();

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("visitant", visitantes != null ? visitantes : Collections.emptyList());

        return "visitors-view/visitantes";
    }

    @GetMapping("/visitantes/edit/{id}")
    public String showEditVisitantesForm(@PathVariable Long id, Model model, Principal principal,
            RedirectAttributes redirectAttributes) {
        Visitante visitante = VisitanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        try {

            if (!apartamentosService.existsByApartamento(visitante.getApartamento().getApartamento())) {
                model.addAttribute("error", "Apartamento no valido");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("visitantes", visitante);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/visitant";
            }
            if (!visitante.getApartamento().isActive()) {
                model.addAttribute("error", "Apartamento inactivo pillín");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("visitantes", visitante);
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "visitors-view/visitant";
            }
            model.addAttribute("vigilant", vigilant);
            model.addAttribute("visitantes", visitante);
            model.addAttribute("apartamentos", apartamentosService.listAllApartments());

            return "visitors-view/visitant";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el visitante");
            return "redirect:/visitors-view/visitant";
        }
    }

    @GetMapping("/visitantes/new")
    public String showNewvisitantForm(Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("visitantes", new Visitante());
        model.addAttribute("apartamentos", apartamentosService.listAllApartments());

        return "visitors-view/visitant";
    }

    //Eliminar una reserva de cancha
    @PostMapping("/reservas/delete/{id}")
    public String deleteReserva(@PathVariable Long id, Principal principal, 
            RedirectAttributes redirectAttributes) {

        try {
            reservaService.delete(id);
            redirectAttributes.addFlashAttribute("error", "Reserva eliminada exitosamente.");
            return "redirect:/vigilant/reservas"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reserva: " + e.getMessage());
            return "redirect:/vigilant/reservas";
        }
    }

    //Guardar una nueva reserva
    @PostMapping("/reservas")
    public String saveReservas(
            @RequestParam("apartamentoId") long numero, @ModelAttribute ReservaCancha reserva, Model model,
            Principal principal, RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        model.addAttribute("reserva", new ReservaCancha());

        Apartamentos apartamento = apartamentosService.findById(numero);
        try {
            if (apartamento == null) {
                model.addAttribute("error", "Apartamento no encontrado.");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("reservas", reservaService.findAll());
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "vigilant-view/reserves";
            } else if (reservaService.VerificarSiEstaenelRango(reserva.getCreatedAt(), reserva.getCancha())) {
                model.addAttribute("error", "Horario inadecuado");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("reservas", reservaService.findAll());
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "vigilant-view/reserves";

            } else if (!apartamento.isActive()) {
                model.addAttribute("error", "Apartamento inactivo");
                model.addAttribute("vigilant", vigilant);
                model.addAttribute("reservas", reservaService.findAll());
                model.addAttribute("apartamentos", apartamentosService.listAllApartments());

                return "vigilant-view/reserves";
            }

            model.addAttribute("vigilant", vigilant);
            model.addAttribute("reservas", reservaService.findAll());
            model.addAttribute("apartamentos", apartamentosService.listAllApartments());
            redirectAttributes.addFlashAttribute("error", "Guardado Correctamente");
            reserva.setApartamento(apartamento);
            reservaService.save(reserva);
            return "redirect:/vigilant/reservas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la reserva");
            model.addAttribute("vigilant", vigilant);
            model.addAttribute("reservas", reservaService.findAll());
            model.addAttribute("apartamentos", apartamentosService.listAllApartments());
            return "redirect:/vigilant/reservas";
        }
    }

    //Editar una reserva
    @GetMapping("/reservas/edit/{id}")
    public String showEditReservaForm(@PathVariable Long id, Model model, Principal principal,
            RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));

        ReservaCancha reserva = reservaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("reserva", reserva); 
        model.addAttribute("reservas", reservaService.findAll()); 
        model.addAttribute("apartamentos", apartamentosService.listAllApartments());
        return "vigilant-view/reserves";
    }

    //Mostrar la lista de reservas
    @GetMapping("/reservas")
    public String ShowReservas(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        reservaService.BorrarReservasPasadas();
        model.addAttribute("reserva", new ReservaCancha());

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("reservas", reservaService.findAll());
        model.addAttribute("apartamentos", apartamentosService.listAllApartments());

        return "vigilant-view/reserves";
    }

    @GetMapping("/reporte-visitantes")
    public String mostrarReporte(Model model, Principal principal) {
        Vigilant vigilant = vigilantService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Vigilante no encontrado"));
        List<ReporteVisitanteDTO> reporte = reporteVisitanteService.generarReporteVisitantes();

        model.addAttribute("vigilant", vigilant);
        model.addAttribute("reporte", reporte);
        return "vigilant-view/reporte-visitantes";
    }
}