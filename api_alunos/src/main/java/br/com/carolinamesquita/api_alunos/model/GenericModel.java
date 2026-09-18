package br.com.carolinamesquita.api_alunos.model;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass //Torna classe base mapeada (não cria tabela separada)
@Data
public abstract class GenericModel { // abstract = não instanciar sozinho

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // ✅ UUID no lugar de IDENTITY
    @Column(name = "codigo", updatable = false, nullable = false)
    private UUID codigo;
}