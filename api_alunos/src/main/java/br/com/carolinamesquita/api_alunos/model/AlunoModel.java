package br.com.carolinamesquita.api_alunos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_alunos")
@Data
@EqualsAndHashCode(callSuper = true) // Inclui campos da classe pai no equals/hashCode
public class AlunoModel extends GenericModel { // Herda, não implementa

    
    private String nome;
    private Double nota1;
    private Double nota2;
    @JsonIgnore 
    @OneToMany (mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcompanhamentoModel> acomapnhamento;
}