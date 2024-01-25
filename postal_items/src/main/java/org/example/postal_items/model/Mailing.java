package org.example.postal_items.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@Document
public class Mailing {
    @Id
    private String id;
    private long mailingCode;
    private String type;
    private int recipientPostalCode;
    private String recipientAddress;
    private String recipientName;
    private PostOffice currentPostOffice;
    private boolean isReceived;
    private List<PostOffice> postOffices;

    public Mailing() {
        isReceived = false;
        postOffices = new ArrayList<>();
    }

    public MailingStatus getStatus() {
        if(isReceived())
            return MailingStatus.IS_RECEIVED;
        if(currentPostOffice == null)
            return MailingStatus.IN_TRANSIT;
        else if (currentPostOffice.getPostalCode() == recipientPostalCode)
            return MailingStatus.AT_RECIPIENT_POST_OFFICE;
        return MailingStatus.AT_INTERMEDIATE_POST_OFFICE;
    }

    public void setCurrentPostOffice(PostOffice currentPostOffice) {
        this.currentPostOffice = currentPostOffice;
        if (currentPostOffice != null)
            postOffices.add(currentPostOffice);
    }
}
