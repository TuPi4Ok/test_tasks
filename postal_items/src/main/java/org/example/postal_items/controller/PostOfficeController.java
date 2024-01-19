package org.example.postal_items.controller;

import org.apache.coyote.BadRequestException;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.Mapper;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.model.dto.PostOfficeDto;
import org.example.postal_items.service.MailingService;
import org.example.postal_items.service.PostOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostOfficeController {
    @Autowired
    PostOfficeService postOfficeService;
    @Autowired
    Mapper mapper;
    @PostMapping
    public PostOffice createPostOffice(@RequestBody PostOfficeDto postOfficeDto) throws BadRequestException {
        return  postOfficeService.createPostOffice(mapper.map(postOfficeDto));
    }
    @GetMapping
    public List<PostOffice> getPostOffices() {
        return postOfficeService.getPostOffices();
    }
}
