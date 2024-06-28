package controllers;

import DAO.DipendenteDAO;
import DAO.DispositivoDAO;
import entities.Dipendente;
import entities.Dispositivo;
import exceptions.DipendenteNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payloads.DispositivoPayload;

import java.util.List;

@RestController
@RequestMapping("/dispositivi")
public class DispositivoController {

    @Autowired
    private DispositivoDAO dispositivoRepository;

    @Autowired
    private DipendenteDAO dipendenteRepository;

    @PostMapping
    public ResponseEntity<Dispositivo> addDispositivo(@RequestBody Dispositivo dispositivo, @RequestParam(required = false) String email) {
        if (email != null && email.isEmpty()) {
            Dipendente dipendente = dipendenteRepository.findByEmail(email).orElseThrow(() -> new DipendenteNotFound("Dipendente con email " + email + " non trovato"));
            dispositivo.setDipendendente(dipendente);
        }

        Dispositivo saveDispositivo = dispositivoRepository.save(dispositivo);
        return  ResponseEntity.status(HttpStatus.CREATED).body(saveDispositivo);
    }

    @PostMapping("/{email}")
    public ResponseEntity<Dispositivo> addDispositivoWithDipendente(
            @PathVariable String email,
            @RequestBody DispositivoPayload payload){

        Dipendente dipendente = dipendenteRepository.findByEmail(email)
                .orElseThrow(()-> new DipendenteNotFound("Dipendente con email " + email + " non trovato"));

        Dispositivo newDispositivo = new Dispositivo();
        newDispositivo.setTipo(payload.getTipo());
        newDispositivo.setStato(payload.getStato());
        newDispositivo.setDipendendente(dipendente);

        Dispositivo saveDispositivo = dispositivoRepository.save(newDispositivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveDispositivo);

    }

    @GetMapping
    public ResponseEntity<List<Dispositivo>> getAllDispositiviWithDipendenti () {
        List<Dispositivo> dispositivi = dispositivoRepository.findAllWithDipendenteEager();
        if (dispositivi.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dispositivi);
    }
}
