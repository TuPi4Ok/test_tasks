package org.example.postal_items.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOfficeDto {
    private int postalCode;
    private String name;
    private String address;
}
