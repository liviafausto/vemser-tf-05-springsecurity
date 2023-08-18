package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.AtendimentoControllerDoc;
import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoInputDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.service.AtendimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
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
    public ResponseEntity<List<AtendimentoOutputDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.findAll());
    }

    @GetMapping("/{idAtendimento}")
    public ResponseEntity<AtendimentoOutputDTO> buscarAtendimentoPeloId(@Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento) throws EntityNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.findById(idAtendimento));
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<AtendimentoOutputDTO>> bucarAtendimentoPeloIdUsuario(@Positive(message = "Deve ser positivo") @PathVariable Integer idPaciente) throws EntityNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.bucarAtendimentoPeloIdUsuario(idPaciente));
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<AtendimentoOutputDTO>> findAllPaginada(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(atendimentoService.findAllPaginada(pagina, quantidadeRegistros));
    }

    @GetMapping("/paginado/data")
    public ResponseEntity<Page<AtendimentoOutputDTO>> findAllPaginadaByData(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros,
            @RequestParam String dataInicio,
            @RequestParam String dataFinal
    ) throws DataInvalidaException {
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService
                .findAllPaginadaByData(dataInicio, dataFinal, pagina, quantidadeRegistros));
    }

    @PostMapping
    public ResponseEntity<AtendimentoOutputDTO> save(@Valid @RequestBody AtendimentoInputDTO novoAtendimento) throws EntityNotFound {
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoService.save(novoAtendimento));
    }

    @PutMapping("/{idAtendimento}")
    public ResponseEntity<AtendimentoOutputDTO> alterarPeloId(@Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento,
                                                              @Valid @RequestBody AtendimentoInputDTO atendimento) throws EntityNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.update(idAtendimento, atendimento));
    }

    @DeleteMapping("/{idAtendimento}")
    public ResponseEntity<Void> deletarAtendimento(@Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento) throws EntityNotFound {
        atendimentoService.deletarPeloId(idAtendimento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
