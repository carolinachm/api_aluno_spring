package br.com.carolinamesquita.api_alunos.repository;

import org.springframework.stereotype.Repository;

import br.com.carolinamesquita.api_alunos.model.AlunoModel;

@Repository 
public interface AlunoRepository extends GenericRepository<AlunoModel> {
    
}
