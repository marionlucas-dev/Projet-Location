package com.accenture.repository.entity.Utilisateurs;
import com.accenture.shared.Permis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CLIENTS")
public class Client extends UtilisateurConnecte {

    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    private LocalDate dateNaissance;

    private LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    private Permis permis;
    private Boolean desactive;

    @PrePersist
    protected void creationAuto() {
        if (this.dateInscription == null)
            this.dateInscription = LocalDate.now();
    }

}
