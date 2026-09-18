package br.com.carolinamesquita.api_alunos.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data 
public class AcompanhamentoDTO {
    //Atributos
    private LocalDate data;
    private String texto;
    private UUID codigoAluno;
    
}
