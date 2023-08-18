package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.PacienteControllerDoc;
import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteAtendimentosOutputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteInputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteOutputDTO;
import br.com.dbc.wbhealth.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/paciente")
public class PacienteController implements PacienteControllerDoc {
    private final PacienteService pacienteService;

    @Override
    @GetMapping
    public ResponseEntity<Page<PacienteOutputDTO>> findAll(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros) {
        Page<PacienteOutputDTO> pacientesPaginados = pacienteService.findAll(pagina, quantidadeRegistros);
        return new ResponseEntity<>(pacientesPaginados, HttpStatus.OK);
    }

    @Override
    @GetMapping("/atendimentos")
    public ResponseEntity<Page<PacienteAtendimentosOutputDTO>> findAllAtendimentos(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros){
        Page<PacienteAtendimentosOutputDTO> pacienteAtendimentosPaginados =
                pacienteService.findAllAtendimentos(pagina, quantidadeRegistros);
        return new ResponseEntity<>(pacienteAtendimentosPaginados, HttpStatus.OK);
    }

    @Override
    @GetMapping("/by-id")
    public ResponseEntity<PacienteOutputDTO> findById(@RequestParam("idPaciente") @Positive Integer idPaciente)
            throws EntityNotFound {
        PacienteOutputDTO pacienteEncontrado = pacienteService.findById(idPaciente);
        return new ResponseEntity<>(pacienteEncontrado, HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<PacienteOutputDTO> save(@RequestBody @Valid PacienteInputDTO paciente)
            throws BancoDeDadosException, EntityNotFound {
        PacienteOutputDTO pacienteCriado = pacienteService.save(paciente);
        return new ResponseEntity<>(pacienteCriado, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{idPaciente}")
    public ResponseEntity<PacienteOutputDTO> update(@PathVariable @Positive Integer idPaciente,
                                                    @RequestBody @Valid PacienteInputDTO paciente)
            throws EntityNotFound {
        PacienteOutputDTO pacienteAtualizado = pacienteService.update(idPaciente, paciente);
        return new ResponseEntity<>(pacienteAtualizado, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{idPaciente}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Integer idPaciente)
            throws EntityNotFound {
        pacienteService.delete(idPaciente);
        return ResponseEntity.ok().build();
    }

}
