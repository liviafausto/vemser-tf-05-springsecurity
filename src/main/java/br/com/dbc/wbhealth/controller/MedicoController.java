package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.MedicoControllerDoc;
import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.medico.MedicoAtendimentoDTO;
import br.com.dbc.wbhealth.model.dto.medico.MedicoInputDTO;
import br.com.dbc.wbhealth.model.dto.medico.MedicoOutputDTO;
import br.com.dbc.wbhealth.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/medico")
public class MedicoController implements MedicoControllerDoc {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<Page<MedicoOutputDTO>> findAll(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros) {
        return new ResponseEntity<>(medicoService.findAll(pagina, quantidadeRegistros), HttpStatus.OK);
    }

    @GetMapping("/{idMedico}")
    public ResponseEntity<MedicoOutputDTO> findById(@PathVariable @Positive Integer idMedico) throws EntityNotFound{
        MedicoOutputDTO medicoOutputDTO = medicoService.findById(idMedico);
        return new ResponseEntity<>(medicoOutputDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<MedicoOutputDTO> save(@Valid @RequestBody MedicoInputDTO medicoInputDTO)
            throws BancoDeDadosException, EntityNotFound {
        MedicoOutputDTO medicoCriado = medicoService.save(medicoInputDTO);
        return new ResponseEntity<>(medicoCriado, HttpStatus.OK);
    }

    @PutMapping("/{idMedico}")
    public ResponseEntity<MedicoOutputDTO> update(@PathVariable @Positive Integer idMedico,
                                                  @Valid @RequestBody MedicoInputDTO medicoInputDTO) throws EntityNotFound {
        MedicoOutputDTO medicoAtualizado = medicoService.update(idMedico, medicoInputDTO);
        return new ResponseEntity<>(medicoAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{idMedico}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive Integer idMedico) throws EntityNotFound {
        medicoService.delete(idMedico);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{medicoId}/atendimentos")
    public ResponseEntity<List<MedicoAtendimentoDTO>> generateMedicoAtendimento(
            @PathVariable Integer medicoId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFim) throws EntityNotFound {
        List<MedicoAtendimentoDTO> report = medicoService.generateMedicoAtendimento(medicoId, dataInicio, dataFim);
        return ResponseEntity.ok(report);
    }
}
