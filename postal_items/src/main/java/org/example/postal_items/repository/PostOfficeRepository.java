package org.example.postal_items.repository;

import org.example.postal_items.model.PostOffice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostOfficeRepository extends CrudRepository<PostOffice, Long> {

}
