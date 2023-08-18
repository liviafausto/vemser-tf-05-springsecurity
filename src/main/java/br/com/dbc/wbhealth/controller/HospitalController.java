package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.HospitalControllerDoc;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalAtendimentoDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalInputDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalOutputDTO;
import br.com.dbc.wbhealth.service.HospitalService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/hospital")
@Validated
public class HospitalController implements HospitalControllerDoc {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public ResponseEntity<List<HospitalOutputDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.findAll());
    }

    @GetMapping("/atendimentos")
    public ResponseEntity<Page<HospitalAtendimentoDTO>> findHospitaisWithAllAtendimentos(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros){
         Page<HospitalAtendimentoDTO> hospitalAtendimentos =
                 hospitalService.findHospitaisWithAllAtendimentos(pagina, quantidadeRegistros);
         return new ResponseEntity<>(hospitalAtendimentos, HttpStatus.OK);
    }

    @GetMapping("/{idHospital}")
    public ResponseEntity<HospitalOutputDTO> findById(@Positive @PathVariable Integer idHospital)
            throws EntityNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.findById(idHospital));
    }

    @PostMapping
    public ResponseEntity<HospitalOutputDTO> save(@Valid @RequestBody HospitalInputDTO hospital) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalService.save(hospital));
    }

    @PutMapping("/{idHospital}")
    public ResponseEntity<HospitalOutputDTO> update(@Positive @PathVariable Integer idHospital, @Valid @RequestBody HospitalInputDTO hospital)
            throws EntityNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.update(idHospital, hospital));
    }

    @DeleteMapping("/{idHospital}")
    public ResponseEntity<Void> deleteById(@Positive @PathVariable Integer idHospital) throws EntityNotFound {
        hospitalService.deleteById(idHospital);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
