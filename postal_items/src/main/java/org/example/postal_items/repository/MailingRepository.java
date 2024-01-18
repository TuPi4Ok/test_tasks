package org.example.postal_items.repository;

import org.example.postal_items.model.Mailing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepository extends CrudRepository <Mailing, Long> {

}
