package org.example.postal_items.controller;

import jakarta.annotation.Resources;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.Mapper;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.service.MailingService;
import org.example.postal_items.service.PostOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MailingController {
    @Autowired
    private MailingService mailingService;

    @Autowired
    Mapper mapper;

    @PostMapping("post/{post_id}/mailing/}")
    public Mailing createMailing(@RequestBody MailingDto mailingDto, @PathVariable("post_id") long id) {
        return mailingService.createMailing(mapper.map(mailingDto), id);
    }
    @PatchMapping("post/{post_id}/departure/{mailing_id}")
    public Mailing departureMailing(@PathVariable("mailing_id") long id) {
        return mailingService.departureMailing(id);
    }

    @PatchMapping("post/{post_id}/arrival/{mailing_id}")
    public Mailing arrivalMailing(@PathVariable("mailing_id") long mailingId, @PathVariable("post_id") long postId) {
        return mailingService.arrivalMailing(postId, mailingId);
    }

}
