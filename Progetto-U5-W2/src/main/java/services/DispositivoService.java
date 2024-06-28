package services;
import DAO.DipendenteDAO;
import DAO.DispositivoDAO;
import entities.Dipendente;
import entities.Dispositivo;
import exceptions.DipendenteNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DispositivoService {

    @Autowired
    private DispositivoDAO dispositivoRepository;

    @Autowired
    private DipendenteDAO dipendenteRepository;
    public Dispositivo addDispositivo(Dispositivo dispositivo,String emailDipendente) {
        if (emailDipendente != null && !emailDipendente.isEmpty()) {
            Dipendente dipendente = dipendenteRepository.findByEmail((emailDipendente))
                    .orElseThrow(() -> new DipendenteNotFound("Dipendente con email: "+emailDipendente+" non trovato"));
            dispositivo.setDipendendente(dipendente);
        }
        return dispositivoRepository.save(dispositivo);
    }

    public List<Dispositivo> getAllDispositiviWithDipendenti () {
        return dispositivoRepository.findAllWithDipendenteEager();
    }
}
