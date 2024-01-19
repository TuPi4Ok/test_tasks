package org.example.postal_items.service;

import org.apache.coyote.BadRequestException;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostOfficeService {
    @Autowired
    PostOfficeRepository postOfficeRepository;

    public PostOffice getPostOfficeByPostalCode(int postalCode) {
        var postOffice = postOfficeRepository.findPostOfficeByPostalCode(postalCode);
        if(postOffice == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post office with this id not found");
        return postOffice;
    }


    public PostOffice createPostOffice(PostOffice postOffice) throws BadRequestException {
        if(postOfficeRepository.findPostOfficeByPostalCode(postOffice.getPostalCode()) == null)
            return postOfficeRepository.save(postOffice);
        throw new BadRequestException("A post office in that postal code already exists.");
    }

    public List<PostOffice> getPostOffices() {
        return  postOfficeRepository.findAll();
    }

}
