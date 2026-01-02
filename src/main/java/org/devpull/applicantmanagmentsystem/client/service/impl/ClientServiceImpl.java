package org.devpull.applicantmanagmentsystem.client.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.devpull.applicantmanagmentsystem.client.mapper.ClientMapper;
import org.devpull.applicantmanagmentsystem.common.util.AgeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.devpull.applicantmanagmentsystem.client.dto.ClientResponse;
import org.devpull.applicantmanagmentsystem.client.dto.ClientRequest;
import org.devpull.applicantmanagmentsystem.client.entity.Client;
import org.devpull.applicantmanagmentsystem.client.repository.ClientRepository;
import org.devpull.applicantmanagmentsystem.client.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repo;

    private final int lifeExpectancy;

    private final ClientMapper clientMapper;

    public ClientServiceImpl(
            ClientRepository repo,
            ClientMapper clientMapper,
            @Value("${app.life.expectancy-years:73}") int lifeExpectancy
    ) {
        this.repo = repo;
        this.clientMapper = clientMapper;
        this.lifeExpectancy = lifeExpectancy;
    }

    @Override
    public Mono<ClientResponse> create(ClientRequest request) {

        int age = AgeUtils.calculateAge(request.birthDate());

        var e = new Client(
                UUID.randomUUID().toString(),
                request.name(),
                request.lastName(),
                age,
                request.birthDate(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        log.info("I'm here to create a new client: {}", e);

        return repo.save(e)
                .flatMap(saved -> repo.findById(saved.id()))
                .doOnNext(x -> log.info("Persisted client id={}", x.id()))
                .map(client -> clientMapper.toResponse(
                        client,
                        client.birthDate().plusYears(lifeExpectancy)
                ));

    }

    @Override
    public Mono<ClientResponse> getById(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Client not found"
                )))
                .map(client -> clientMapper.toResponse(
                        client,
                        client.birthDate().plusYears(lifeExpectancy)
                ));
    }

    @Override
    public Flux<ClientResponse> getAll() {
        return repo.findAll()
                .map(client -> clientMapper.toResponse(
                        client,
                        client.birthDate().plusYears(lifeExpectancy)
                ));
    }

    @Override
    public LocalDate EventDateEstimate(LocalDate date) {
        return date.plusYears(lifeExpectancy);
    }

    @Override
    public Mono<ClientResponse> updateClient(String id, ClientRequest request) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")))
                .map(existing -> clientMapper.mergeForPut(existing, request))
                .flatMap(repo::save)
                .map(saved -> clientMapper.toResponse(saved, saved.birthDate().plusYears(lifeExpectancy)));
    }

    @Override
    public Mono<Void> deleteClient(String id) {
        return repo.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")))
                .flatMap(u -> repo.deleteById(id));
    }


}
