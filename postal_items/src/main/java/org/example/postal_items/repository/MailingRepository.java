package org.example.postal_items.repository;

import org.example.postal_items.model.Mailing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepository extends MongoRepository<Mailing, String> {
    Mailing findTopByOrderByMailingCodeDesc();
    Mailing findMailingByMailingCode(long mailingCode);
    Mailing findMailingByRecipientName(String name);
}
