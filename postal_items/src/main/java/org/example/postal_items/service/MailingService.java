package org.example.postal_items.service;

import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.MailingStatus;
import org.example.postal_items.model.Mapper;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.repository.MailingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailingService {
    @Autowired
    MailingRepository mailingRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    PostOfficeService postOfficeService;

    public Mailing getMailingById(long id) {
        return mailingRepository.findById(id).orElseThrow();
    }

    public Mailing createMailing(Mailing mailing, long postId) {
        mailing.setCurrentPostOffice(postOfficeService.getPostOfficeById(postId));
        mailing.setPostOffices(List.of(mailing.getCurrentPostOffice()));
        mailing.setStatus(MailingStatus.AT_POST_OFFICE);
        return mailingRepository.save(mailing);
    }

    public Mailing departureMailing(long id) {
        var mailing = getMailingById(id);
        mailing.setCurrentPostOffice(null);
        mailing.setStatus(MailingStatus.IN_TRANSIT);
        return mailing;
    }


    public Mailing arrivalMailing(long postId, long mailingId) {
        PostOffice postOffice = postOfficeService.getPostOfficeById(postId);
        Mailing mailing = getMailingById(mailingId);
        return null;
    }
}
