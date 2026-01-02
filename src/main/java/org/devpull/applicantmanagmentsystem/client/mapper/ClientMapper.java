package org.devpull.applicantmanagmentsystem.client.mapper;

import org.devpull.applicantmanagmentsystem.client.dto.ClientRequest;
import org.devpull.applicantmanagmentsystem.client.dto.ClientResponse;
import org.devpull.applicantmanagmentsystem.client.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.devpull.applicantmanagmentsystem.common.util.AgeUtils;

import java.time.LocalDate;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, imports = AgeUtils.class)
public interface ClientMapper {

    @Mapping(target = "id", expression = "java(id)")
    @Mapping(target = "age", expression = "java(AgeUtils.calculateAge(request.birthDate()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toNewEntity(ClientRequest request, String id);

    // PUT
    @Mapping(target = "id", source = "existing.id")
    @Mapping(target = "createdAt", source = "existing.createdAt")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "age", expression = "java(AgeUtils.calculateAge(request.birthDate()))")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "birthDate", source = "request.birthDate")
    Client mergeForPut(Client existing, ClientRequest request);

    // TODO : Response with estimated event date required - what is the fechaEstimadaEvento ???
    @Mapping(target = "estimateEventDate", source = "estimateEventDate")
    ClientResponse toResponse(Client client, LocalDate estimateEventDate);

}
