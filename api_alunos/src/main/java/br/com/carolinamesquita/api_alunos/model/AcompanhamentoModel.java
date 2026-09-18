package br.com.carolinamesquita.api_alunos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity 
@Table (name = "tb_acompanhamento")
@Data 
@EqualsAndHashCode(callSuper = true) 
public class AcompanhamentoModel extends GenericModel {

    private LocalDate data;
    private String texto;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="codigo_aluno")
    private AlunoModel aluno;
    
}
