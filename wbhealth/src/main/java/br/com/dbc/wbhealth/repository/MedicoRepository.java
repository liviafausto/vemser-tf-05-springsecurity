package br.com.dbc.wbhealth.repository;

import br.com.dbc.wbhealth.model.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicoRepository extends JpaRepository<MedicoEntity, Integer> {
    Boolean existsByCrm(String crm);
}
