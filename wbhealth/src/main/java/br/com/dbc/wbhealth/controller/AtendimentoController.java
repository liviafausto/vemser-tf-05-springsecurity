package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.AtendimentoControllerDoc;
import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoInputDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.service.AtendimentoService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/atendimento")
@RequiredArgsConstructor
public class AtendimentoController implements AtendimentoControllerDoc {

    private final AtendimentoService atendimentoService;

    @GetMapping
    public ResponseEntity<Page<AtendimentoOutputDTO>> findAllPaginada(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) {
        Page<AtendimentoOutputDTO> atendimentos = atendimentoService.findAllPaginada(pagina, quantidadeRegistros);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentos);
    }

    @GetMapping("/data")
    public ResponseEntity<Page<AtendimentoOutputDTO>> findAllPaginadaByData(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros,
            @RequestParam String dataInicio,
            @RequestParam String dataFinal
    ) throws DataInvalidaException {
        Page<AtendimentoOutputDTO> atendimentosEntreDatas =
                atendimentoService.findAllPaginadaByData(dataInicio, dataFinal, pagina, quantidadeRegistros);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentosEntreDatas);
    }

    @GetMapping("/{idAtendimento}")
    public ResponseEntity<AtendimentoOutputDTO> buscarAtendimentoPeloId(
            @Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento
    ) throws EntityNotFound {
        AtendimentoOutputDTO atendimentoEncontrado = atendimentoService.findById(idAtendimento);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoEncontrado);
    }

    @GetMapping("/paciente/{cpfPaciente}")
    public ResponseEntity<Page<AtendimentoOutputDTO>> bucarAtendimentoPeloCpfPaciente(
            @CPF @PathVariable String cpfPaciente,
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) throws EntityNotFound {
        Page<AtendimentoOutputDTO> atendimentosPaciente = atendimentoService
                .bucarAtendimentoPeloCpfPaciente(cpfPaciente, pagina, quantidadeRegistros);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentosPaciente);
    }

    @GetMapping("/medico/{cpfMedico}")
    public ResponseEntity<Page<AtendimentoOutputDTO>> bucarAtendimentoPeloCpfMedico(
            @CPF @PathVariable String cpfMedico,
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) throws EntityNotFound{
        Page<AtendimentoOutputDTO> atendimentosMedico = atendimentoService
                .buscarAtendimentoPeloCpfMedico(cpfMedico, pagina, quantidadeRegistros);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentosMedico);
    }

    @PostMapping
    public ResponseEntity<AtendimentoOutputDTO> save(
            @Valid @RequestBody AtendimentoInputDTO novoAtendimento
    ) throws EntityNotFound {
        AtendimentoOutputDTO atendimentoCriado = atendimentoService.save(novoAtendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoCriado);
    }

    @PutMapping("/{idAtendimento}")
    public ResponseEntity<AtendimentoOutputDTO> alterarPeloId(
            @Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento,
            @Valid @RequestBody AtendimentoInputDTO atendimento
    ) throws EntityNotFound {
        AtendimentoOutputDTO atendimentoAtualizado = atendimentoService.update(idAtendimento, atendimento);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoAtualizado);
    }

    @DeleteMapping("/{idAtendimento}")
    public ResponseEntity<Void> deletarAtendimento(
            @Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento
    ) throws EntityNotFound {
        atendimentoService.deletarPeloId(idAtendimento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
