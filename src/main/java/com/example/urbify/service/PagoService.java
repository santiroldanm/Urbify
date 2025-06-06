package com.example.urbify.service;

import com.example.urbify.models.Pago;
import com.example.urbify.models.Apartamentos;
import com.example.urbify.repository.PagoRepository;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Page<Pago> obtenerPagos(Pageable pageable) { 
        return pagoRepository.findAll(pageable);
    }

    public String obtenerMesPorPagoId(Long id) {
        return pagoRepository.findMesById(id);
    }

    public List<Pago> getByEstado(String estado){
        return pagoRepository.findByEstado(estado);
    }

    public List<String> obtenerMesesPermitidosDesde(String mesInicio) {
        List<String> todosLosMeses = List.of("Abril", "Mayo", "Junio");

        int indiceInicio = -1;
        for (int i = 0; i < todosLosMeses.size(); i++) {
            if (todosLosMeses.get(i).equalsIgnoreCase(mesInicio.trim())) {
                indiceInicio = i;
                break;
            }
        }

        if (indiceInicio == -1) {
            return todosLosMeses;
        }
        return todosLosMeses.subList(indiceInicio, todosLosMeses.size());
    }

    public List<Apartamentos> obtenerApartamentosConPagosMorososOPendientes() {
        return pagoRepository.findApartamentosConPagosMorososOPendientes();
    }

    public Pago findById(Long id) {
        return pagoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    @Transactional
    public void registrarOPActualizarPago(Pago pago, List<String> mesesPagados, String metodoPago) {
        double valorMensual = 550000.0;
        int totalMeses = 3; 

        List<Integer> mesesNumericos = mesesPagados.stream()
                .map(this::convertirMesANumero)
                .sorted()
                .collect(Collectors.toList());

        if (mesesNumericos.isEmpty()) {
            return;
        }


        Optional<Pago> pagoExistenteOpt = pagoRepository
                .findTopByApartamentoOrderByUpdatedAtDesc(pago.getApartamento());

        Pago pagoExistente;

        if (pagoExistenteOpt.isPresent()) {
            pagoExistente = pagoExistenteOpt.get();
        } else {
            pagoExistente = new Pago();
            pagoExistente.setApartamento(pago.getApartamento());
            pagoExistente.setCreatedAt(new Date());
        }

        int ultimoMesPagado = mesesNumericos.get(mesesNumericos.size() - 1);
        boolean incluyeJunio = mesesNumericos.contains(6);

        if (ultimoMesPagado < 6) {
            pagoExistente.setMes(convertirNumeroAMes(ultimoMesPagado + 1));
        } else {
            pagoExistente.setMes("Junio");
        }

        int mesesPagadosCount = mesesNumericos.size();
        int mesesFaltantes = totalMeses - mesesPagadosCount;

        double valorPendiente;

        if (incluyeJunio) {
            valorPendiente = valorMensual;
        } else {
            String mesPendiente = convertirNumeroAMes(ultimoMesPagado + 1);
            if (mesPendiente.equalsIgnoreCase("Junio")) {
                valorPendiente = valorMensual; 
            } else {
                valorPendiente = mesesFaltantes * valorMensual;
            }
        }
        pagoExistente.setValor(valorPendiente);

        if (incluyeJunio) {
            pagoExistente.setEstado("PAGADO");
        } else {
            String mesPendiente = convertirNumeroAMes(ultimoMesPagado + 1);
            if (mesPendiente.equalsIgnoreCase("Junio")) {
                pagoExistente.setEstado("PENDIENTE");
            } else {
                pagoExistente.setEstado("MOROSO");
            }
        }

        pagoExistente.setMetodoPago(metodoPago);
        pagoExistente.setFechaPago(new Date());
        pagoExistente.setUpdatedAt(new Date());

        pagoRepository.save(pagoExistente);
    }

    private int convertirMesANumero(String mes) {
        switch (mes.trim().toUpperCase()) {
            case "ABRIL":
                return 4;
            case "MAYO":
                return 5;
            case "JUNIO":
                return 6;
            default:
                throw new IllegalArgumentException("Mes no permitido: " + mes);
        }
    }

    private String convertirNumeroAMes(int numeroMes) {
        switch (numeroMes) {
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            default:
                throw new IllegalArgumentException("Número de mes no permitido: " + numeroMes);
        }
    }

}