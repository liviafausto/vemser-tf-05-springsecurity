package br.com.dbc.wbhealth.repository;

import br.com.dbc.wbhealth.model.entity.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Integer> {

}
