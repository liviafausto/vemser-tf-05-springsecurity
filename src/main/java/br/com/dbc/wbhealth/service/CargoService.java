package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.entity.CargoEntity;
import br.com.dbc.wbhealth.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {
    private final CargoRepository cargoRepository;

    public CargoEntity findById(Integer idCargo) throws EntityNotFound {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() ->
                        new EntityNotFound("Cargo não encontrado: " + idCargo));
    }

}

