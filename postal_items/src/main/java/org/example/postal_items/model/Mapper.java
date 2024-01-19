package org.example.postal_items.model;

import liquibase.command.CommandOverride;
import org.example.postal_items.model.dto.MailingDto;
import org.example.postal_items.model.dto.PostOfficeDto;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@org.mapstruct.Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@Component
public abstract class Mapper {
    public abstract Mailing map(MailingDto mailingDto);
    public abstract PostOffice map(PostOfficeDto postOfficeDto);
}
