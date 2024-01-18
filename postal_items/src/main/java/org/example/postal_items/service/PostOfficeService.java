package org.example.postal_items.service;

import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.repository.MailingRepository;
import org.example.postal_items.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class PostOfficeService {
    @Autowired
    PostOfficeRepository postOfficeRepository;

    public PostOffice getPostOfficeById(long id) {
        return postOfficeRepository.findById(id).orElseThrow();
    }


}
