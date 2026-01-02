package org.devpull.applicantmanagmentsystem.client.dto;

import org.devpull.applicantmanagmentsystem.client.entity.Client;
import java.time.LocalDate;

public record ClientResponse(
        String id,
        String name,
        String lastName,
        Integer age,
        LocalDate birthDate,
        LocalDate estimateEventDate
) {
    //
    public static ClientResponse from(Client e, LocalDate event) {
        return new ClientResponse(e.id(), e.name(), e.lastName(), e.age(), e.birthDate(), event);
    }
}
