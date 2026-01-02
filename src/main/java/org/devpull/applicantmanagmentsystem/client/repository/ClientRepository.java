package org.devpull.applicantmanagmentsystem.client.repository;

import org.devpull.applicantmanagmentsystem.client.entity.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ClientRepository extends ReactiveCrudRepository<Client, String>  {

}
