package org.devpull.applicantmanagmentsystem.client.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Table("clients")
public record Client(
       @Id
       String id,

       String name,

       @Column("last_name")
       String lastName,

       Integer age,

       @Column("birth_date")
       LocalDate birthDate,

       @Column("created_at")
       OffsetDateTime createdAt,

       @Column("updated_at")
       OffsetDateTime updatedAt) implements Persistable<String> {

       @Override
       public String getId() {
              return id;
       }

       @Override
       public boolean isNew() {
              return createdAt == null;
       }
}
