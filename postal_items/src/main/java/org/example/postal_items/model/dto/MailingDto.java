package org.example.postal_items.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailingDto {
    private String type;
    private long recipientPostalCode;
    private String recipientAddress;
    private String recipientName;
}
