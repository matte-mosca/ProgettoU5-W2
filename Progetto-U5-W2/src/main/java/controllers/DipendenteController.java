package controllers;

import DAO.DipendenteDAO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Dipendente;
import exceptions.DipendenteNotFound;
import exceptions.InvalidEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Autowired
    private DipendenteDAO dipendenteRepository;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public List<Dipendente> getAllDipendenti() {
        List<Dipendente> result = dipendenteRepository.findAll();
        if (result.isEmpty()) {
            throw new DipendenteNotFound("Nessun dipendente trovato");
        }
        return result;
    }

    @GetMapping("/{email}")
    public Dipendente getByEmail(@PathVariable String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmail("l'email: "+email+" non è valida");
        }
        return dipendenteRepository.findByEmail(email)
                .orElseThrow(()-> new DipendenteNotFound("dipendente con email: " +email+ " non trovato"));
    }

    @PostMapping
    public Dipendente addDipendente (@RequestBody Dipendente dipendente) {
        if (EMAIL_PATTERN.matcher(dipendente.getEmail()).matches()){
            throw new InvalidEmail("L'email "+dipendente.getEmail()+ " non è valida");
        }
        return dipendenteRepository.save(dipendente);
    }

    @PutMapping("/{email}/avatar")
    public Dipendente updateDipendenteAvatar(@PathVariable String email, @RequestParam("file")MultipartFile file) throws IOException {
        System.out.println("Nome file: " + file.getOriginalFilename());
        System.out.println("Dimensione file "+ file.getSize());
        if (!file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            String newAvatarURL = (String) uploadResult.get("url");

            Dipendente dipendente = dipendenteRepository.findByEmail(email)
                    .orElseThrow(()-> new DipendenteNotFound("Dipendente con email: "+email+" non trovato"));

            dipendente.setAvatar((newAvatarURL));
            return dipendenteRepository.save(dipendente);
        } else {
            throw new IllegalArgumentException("File non presente");
        }
    }
}
