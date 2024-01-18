package org.example.postal_items.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mailing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    private long recipientPostalCode;
    private String recipientAddress;
    private String recipientName;
    @Enumerated
    private MailingStatus status;
    @ManyToOne
    @JoinColumn(name = "postal_code")
    private PostOffice currentPostOffice;
    @ManyToMany(mappedBy = "mailings")
    private List<PostOffice> postOffices;
}
