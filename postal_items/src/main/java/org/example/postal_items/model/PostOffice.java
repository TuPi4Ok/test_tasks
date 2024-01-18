package org.example.postal_items.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long PostalCode;
    private String name;
    private String address;
    private boolean received;
    @OneToMany(mappedBy = "currentPostOffice")
    private List<Mailing> currentMailings;
    @ManyToMany
    @JoinTable(
            name = "mailings_post_offices",
            joinColumns = @JoinColumn(name = "postal_code"),
            inverseJoinColumns = @JoinColumn(name = "mailing_id")
    )
    private List<Mailing> mailings;
}
