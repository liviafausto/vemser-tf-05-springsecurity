package br.com.dbc.wbhealth.repository;

import br.com.dbc.wbhealth.model.entity.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaEntity, Integer> {
    Boolean existsByCpf(String cpf);
    Optional<PessoaEntity> findByCpf(String cpf);

}
