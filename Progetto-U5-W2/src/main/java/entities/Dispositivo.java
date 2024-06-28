package entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dispositivi")
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String stato;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "dipendente_email")
    private Dipendente dipendendente;

    @Override
    public String toString() {
        return "Dispositivo{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", stato='" + stato + '\'' +
                ", dipendenteEmail=" + (dipendendente != null ? dipendendente.getEmail() : "None") +
                '}';
    }
}
