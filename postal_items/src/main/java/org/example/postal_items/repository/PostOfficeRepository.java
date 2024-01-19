package org.example.postal_items.repository;

import org.example.postal_items.model.PostOffice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostOfficeRepository extends MongoRepository<PostOffice, String> {
    public PostOffice findPostOfficeByPostalCode(int postalCode);
}
