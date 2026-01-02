package org.devpull.applicantmanagmentsystem.client;

import org.devpull.applicantmanagmentsystem.client.dto.ClientRequest;
import org.devpull.applicantmanagmentsystem.client.dto.ClientResponse;
import org.devpull.applicantmanagmentsystem.client.entity.Client;
import org.devpull.applicantmanagmentsystem.client.mapper.ClientMapper;
import org.devpull.applicantmanagmentsystem.client.repository.ClientRepository;
import org.devpull.applicantmanagmentsystem.client.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock ClientRepository repo;
    @Mock ClientMapper clientMapper;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(repo, clientMapper, 73); // lifeExpectancy = 73
    }

    @Test
    void getById_whenFound_returnsResponse() {
        var client = new Client(
                "id-1", "Luis", "Gutierrez", 30,
                LocalDate.of(1995, 5, 10),
                null, null
        );

        var resp = new ClientResponse(
                "id-1", "Luis", "Gutierrez", 30,
                client.birthDate(),
                client.birthDate().plusYears(73)
        );

        when(repo.findById("id-1")).thenReturn(Mono.just(client));
        when(clientMapper.toResponse(client, client.birthDate().plusYears(73))).thenReturn(resp);

        StepVerifier.create(service.getById("id-1"))
                .expectNext(resp)
                .verifyComplete();
    }

    @Test
    void getById_whenMissing_returns404() {
        when(repo.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(service.getById("missing"))
                .expectErrorSatisfies(err -> {
                    assertThat(err).isInstanceOf(ResponseStatusException.class);
                    var ex = (ResponseStatusException) err;
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                })
                .verify();
    }

    @Test
    void create_saves_and_returnsResponse() {
        var req = new ClientRequest("Luis", "Gutierrez", LocalDate.of(1995, 5, 10));

        var saved = new Client(
                "id-1", "Luis", "Gutierrez", 30, req.birthDate(), null, null
        );

        var resp = new ClientResponse(
                "id-1", "Luis", "Gutierrez", 30, req.birthDate(), req.birthDate().plusYears(73)
        );

        when(repo.save(any(Client.class))).thenReturn(Mono.just(saved));
        when(repo.findById("id-1")).thenReturn(Mono.just(saved));
        when(clientMapper.toResponse(saved, req.birthDate().plusYears(73))).thenReturn(resp);

        StepVerifier.create(service.create(req))
                .expectNext(resp)
                .verifyComplete();
    }

    @Test
    void updateClient_updates_and_returnsResponse() {
        var existing = new Client("id-1", "Luis", "Gutierrez", 30, LocalDate.of(1995, 5, 10), null, null);
        var req = new ClientRequest("Luis", "Guti", LocalDate.of(1995, 5, 10));

        var merged = new Client("id-1", "Luis", "Guti", 30, req.birthDate(), null, OffsetDateTime.now());
        var resp = new ClientResponse("id-1", "Luis", "Guti", 30, req.birthDate(), req.birthDate().plusYears(73));

        when(repo.findById("id-1")).thenReturn(Mono.just(existing));
        when(clientMapper.mergeForPut(existing, req)).thenReturn(merged);
        when(repo.save(merged)).thenReturn(Mono.just(merged));
        when(clientMapper.toResponse(merged, req.birthDate().plusYears(73))).thenReturn(resp);

        StepVerifier.create(service.updateClient("id-1", req))
                .expectNext(resp)
                .verifyComplete();
    }


}

