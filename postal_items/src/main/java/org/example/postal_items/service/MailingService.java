package org.example.postal_items.service;

import org.apache.coyote.BadRequestException;
import org.example.postal_items.model.Mailing;
import org.example.postal_items.model.MailingStatus;
import org.example.postal_items.model.PostOffice;
import org.example.postal_items.repository.MailingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MailingService {
    MailingRepository mailingRepository;
    PostOfficeService postOfficeService;
    private long mailingCodeCounter;

    @Autowired
    public MailingService(MailingRepository mailingRepository, PostOfficeService postOfficeService) {
        this.mailingRepository = mailingRepository;
        this.postOfficeService = postOfficeService;

        var topMailing = mailingRepository.findTopByOrderByMailingCodeDesc();
        if (topMailing != null) {
            mailingCodeCounter = topMailing.getMailingCode();
        } else {
            mailingCodeCounter = 0;
        }
    }

    public long getNextMailingCode() {
        return ++mailingCodeCounter;
    }

    public Mailing getMailingByMailingCode(long mailingCode) {
        Mailing mailing = mailingRepository.findMailingByMailingCode(mailingCode);
        if(mailing == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mailing with this id not found");
        return mailing;
    }

    public void departureValidateMailing(Mailing mailing, int postalCode) throws BadRequestException {
        if (mailing.getCurrentPostOffice() == null)
            throw new BadRequestException("Mailing in transit");
        validateMailing(mailing, postalCode);
    }

    public void arrivalValidateMailing(Mailing mailing, int postalCode) throws BadRequestException {
        if (mailing.getCurrentPostOffice() == null)
            return;
        validateMailing(mailing, postalCode);
        if(mailing.getPostOffices().contains(mailing.getCurrentPostOffice()))
            throw new BadRequestException("Mailing has already been to this post office");
    }

    public void validateMailing(Mailing mailing, int postalCode) throws BadRequestException {
        if (mailing.isReceived())
            throw new BadRequestException("Mailing has already been delivered");
        if(mailing.getCurrentPostOffice().getPostalCode() !=
                postOfficeService.getPostOfficeByPostalCode(postalCode).getPostalCode())
            throw new BadRequestException("This parcel is in a different post office.");
    }

    public Mailing createMailing(Mailing mailing, int postalCode) throws BadRequestException {
        try {
            postOfficeService.getPostOfficeByPostalCode(mailing.getRecipientPostalCode());
        } catch (ResponseStatusException e) {
            throw new BadRequestException("The recipient's post office does not exist.");
        }
        mailing.setCurrentPostOffice(postOfficeService.getPostOfficeByPostalCode(postalCode));
        mailing.setMailingCode(getNextMailingCode());
        mailing = mailingRepository.save(mailing);
        return mailing;
    }

    public Mailing departureMailing(long mailingCode, int postalCode) throws BadRequestException {
        var mailing = getMailingByMailingCode(mailingCode);
        departureValidateMailing(mailing, postalCode);
        mailing.setCurrentPostOffice(null);
        return mailingRepository.save(mailing);
    }


    public Mailing arrivalMailing(int postalCode, long mailingCode) throws BadRequestException {
        var mailing = getMailingByMailingCode(mailingCode);
        arrivalValidateMailing(mailing, postalCode);
        mailing.setCurrentPostOffice(postOfficeService.getPostOfficeByPostalCode(postalCode));
        return mailingRepository.save(mailing);
    }

    public Mailing receiptMailing(long mailingCode) throws BadRequestException {
        Mailing mailing = getMailingByMailingCode(mailingCode);
        mailing.setReceived(true);
        if(mailing.getStatus() == MailingStatus.AT_RECIPIENT_POST_OFFICE)
            return mailingRepository.save(mailing);
        throw new BadRequestException("Mailing is at another post office");
    }
}
