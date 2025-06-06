package com.example.urbify.controller;

import com.example.urbify.models.Admin;
import com.example.urbify.models.Apartamentos;
import com.example.urbify.models.Pago;
import com.example.urbify.repository.PagoRepository;
import com.example.urbify.service.AdminService;
import com.example.urbify.service.ApartamentosService;
import com.example.urbify.service.PagoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ApartamentosService apartamentosService;

    @Autowired
    private PagoService pagoService;

    //Muestra la lista de pagos
    @GetMapping("/payments")
    public String showPaymentsList(
            Model model,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String mes,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer torre) {

        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);

        Pageable pageable = PageRequest.of(page, size, Sort.by("mes").descending());
        Page<Pago> pagosPage;

        if (mes != null && mes.trim().isEmpty())
            mes = null;
        if (estado != null && estado.trim().isEmpty())
            estado = null;

        String keywordFormatted = (keyword != null && !keyword.trim().isEmpty())
                ? keyword.toLowerCase()
                : null;

        pagosPage = pagoRepository.buscarPagosConFiltros(
                keywordFormatted,
                mes,
                estado,
                torre,
                pageable);

        if (keywordFormatted != null || mes != null || estado != null || torre != null) {
            pagosPage = pagoRepository.buscarPagosConFiltros(
                    keywordFormatted,
                    mes,
                    estado,
                    torre,
                    pageable);
        } else {
            pagosPage = pagoRepository.findAll(pageable);
        }

        Long totalRecaudado = pagoRepository.getTotalRecaudado();
        Long deudaTotal = pagoRepository.getDeudaTotal();
        Long aptosMorosos = pagoRepository.countApartamentosMorosos();
        Long pagosDelMes = pagoRepository.countPagosDelMes();

        int totalPages = pagosPage.getTotalPages();
        int currentPage = pagosPage.getNumber();
        int startPage = Math.max(0, currentPage - 1);
        int endPage = Math.min(totalPages - 1, currentPage + 1);

        model.addAttribute("admin", admin);
        model.addAttribute("pagosPage", pagosPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("mes", mes);
        model.addAttribute("estado", estado);
        model.addAttribute("torre", torre);

        model.addAttribute("totalRecaudado", totalRecaudado != null ? totalRecaudado : 0);
        model.addAttribute("deudaTotal", deudaTotal != null ? deudaTotal : 0);
        model.addAttribute("aptosMorosos", aptosMorosos != null ? aptosMorosos : 0);
        model.addAttribute("pagosDelMes", pagosDelMes != null ? pagosDelMes : 0);

        return "admin-view/payments";
    }

    //Permite editar un pago (pagar)
    @GetMapping("/payments/edit/{id}")
    public String showEditPaymentForm(@PathVariable Long id, Model model, Principal principal) {
        String email = principal.getName();
        Admin admin = adminService.findByEmail(email);

        Pago pagoExistente = pagoService.findById(id);
        String mesPago = pagoService.obtenerMesPorPagoId(id); 
        List<Apartamentos> aptos = pagoService.obtenerApartamentosConPagosMorososOPendientes();

        List<String> mesesPermitidos = pagoService.obtenerMesesPermitidosDesde(mesPago);

        model.addAttribute("admin", admin);
        model.addAttribute("pago", pagoExistente);
        model.addAttribute("apartamento", pagoExistente.getApartamento());
        model.addAttribute("apartamentosMorososOPendientes", aptos);
        model.addAttribute("primerMes", mesPago);
        model.addAttribute("mesesPermitidos", mesesPermitidos);

        return "admin-view/payment_form";
    }

    //Guarda el pago
    @PostMapping("/payments")
    public String registrarPago(
            @ModelAttribute("pago") Pago pago,
            @RequestParam("apartamentoId") Long apartamentoId,
            @RequestParam("mes") List<String> mesesPagados,
            @RequestParam("metodoPago") String metodoPago) {

        Apartamentos apartamento = apartamentosService.findById(apartamentoId);
        if (apartamento == null) {
            throw new RuntimeException("Apartamento no encontrado");
        }

        pago.setApartamento(apartamento);

        pagoService.registrarOPActualizarPago(pago, mesesPagados, metodoPago);

        return "redirect:/payments";
    }

    @GetMapping("/payments/{id}/recibo")
public String verRecibo(@PathVariable Long id, Model model) {
    Pago pago = pagoService.findById(id);
    model.addAttribute("pago", pago);
    return "admin-view/recibo";
}

}