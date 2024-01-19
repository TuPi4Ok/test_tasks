package org.example.postal_items.controller;

import org.apache.coyote.BadRequestException;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.Mapper;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

@RestController
public class MailingController {
    @Autowired
    private MailingService mailingService;

    @Autowired
    Mapper mapper;

    @PostMapping("post/{post_id}/mailing")
    public Mailing createMailing(@RequestBody MailingDto mailingDto, @PathVariable("post_id") int postalCode) throws BadRequestException {
            return mailingService.createMailing(mapper.map(mailingDto), postalCode);
    }
    @PatchMapping("post/{post_id}/departure/{mailing_id}")
    public Mailing departureMailing(@PathVariable("mailing_id") long mailingCode, @PathVariable("post_id") int postalCode) throws BadRequestException, ChangeSetPersister.NotFoundException {
        return mailingService.departureMailing(mailingCode, postalCode);
    }

    @PatchMapping("post/{post_id}/arrival/{mailing_id}")
    public Mailing arrivalMailing(@PathVariable("mailing_id") long mailingCode, @PathVariable("post_id") int postalCode) throws ChangeSetPersister.NotFoundException, BadRequestException {
        return mailingService.arrivalMailing(postalCode, mailingCode);
    }

    @GetMapping("/mailing/{mailing_id}")
    public Mailing getMailing(@PathVariable("mailing_id") long mailingCode) {
        return mailingService.getMailingByMailingCode(mailingCode);
    }

    @PatchMapping("/mailing/{mailing_id}")
    public Mailing receiptMailing(@PathVariable("mailing_id") long mailingCode) throws BadRequestException {
        return mailingService.receiptMailing(mailingCode);
    }
}
