package org.devpull.applicantmanagmentsystem.client.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.devpull.applicantmanagmentsystem.client.dto.ApiMessage;
import org.devpull.applicantmanagmentsystem.client.dto.ClientRequest;
import org.devpull.applicantmanagmentsystem.client.dto.ClientResponse;
import org.devpull.applicantmanagmentsystem.client.dto.MetricsResponse;
import org.devpull.applicantmanagmentsystem.client.repository.MetricsRepository;
import org.devpull.applicantmanagmentsystem.client.service.ClientService;
import org.devpull.applicantmanagmentsystem.docs.openapi.OpenApiConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Clients")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class ClientController {

    private final ClientService service;

    private final MetricsRepository metricsRepository;

    public ClientController(ClientService service,
                            MetricsRepository metricsRepository) {
        this.service = service;
        this.metricsRepository = metricsRepository;
    }

    @GetMapping
    public Flux<ClientResponse> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ClientResponse> get(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClientResponse> create(@Valid @RequestBody ClientRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public Mono<ClientResponse> update(@PathVariable String id, @RequestBody ClientRequest req) {
        return service.updateClient(id, req);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiMessage>> delete(@PathVariable String id) {
        return service.deleteClient(id)
                .thenReturn(ResponseEntity.ok(
                        new ApiMessage("Client deleted successfully")
                ));
    }


    /********** METRICS  **********/

    @GetMapping("/metrics")
    public Mono<MetricsResponse> metrics() {
        return metricsRepository.metrics()
                .map(r -> new MetricsResponse(r.quantity(),
                                                             r.average(),
                                                             r.standardDeviation()));
    }

}
