package br.com.carolinamesquita.api_alunos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.UUID;

@NoRepositoryBean // ✅ Não cria implementação concreta para esta interface
public interface GenericRepository<T> extends JpaRepository<T, UUID> {
    // Já herda automaticamente todos os métodos do JpaRepository:
    // save(), findById(), findAll(), deleteById(), count(), existsById() etc.
}