package org.devpull.applicantmanagmentsystem.client.service;

import org.devpull.applicantmanagmentsystem.client.dto.ClientResponse;
import org.devpull.applicantmanagmentsystem.client.dto.ClientRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ClientService {

    Mono<ClientResponse> create(ClientRequest request);

    Mono<ClientResponse> getById(String id);

    Flux<ClientResponse> getAll();

    LocalDate EventDateEstimate(LocalDate date);

    Mono<ClientResponse> updateClient(String id, ClientRequest request);

    Mono<Void> deleteClient(String id);

}
