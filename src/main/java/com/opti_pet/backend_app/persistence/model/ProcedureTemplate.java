package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "procedure_template", schema = "opti-pet")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @ManyToOne
    @JoinColumn(name = "bill_template_id")
    private BillTemplate billTemplate;
}
