package org.example.postal_items.repository;

import org.example.postal_items.model.Mailing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepository extends MongoRepository<Mailing, String> {
    public Mailing findTopByOrderByMailingCodeDesc();
    public Mailing findMailingByMailingCode(long mailingCode);
}
