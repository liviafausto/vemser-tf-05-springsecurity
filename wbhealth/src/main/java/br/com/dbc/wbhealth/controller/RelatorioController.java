package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.RelatorioControllerDoc;
import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.model.dto.relatorio.RelatorioLucro;
import br.com.dbc.wbhealth.service.AtendimentoServiceRelatorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@RequiredArgsConstructor

@RestController
@RequestMapping("/relatorio")
public class RelatorioController implements RelatorioControllerDoc {

    private final AtendimentoServiceRelatorio atendimentoService;

    @GetMapping("/lucro")
    public ResponseEntity<Page<RelatorioLucro>> relatorioLucroAteOMomento(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) {
        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.findLucroAteAgora(paginacao));
    }

    @GetMapping("/lucro/data")
    public ResponseEntity<Page<RelatorioLucro>> relatorioLucroPorData(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros,
            @RequestParam String dataInicio
    ) throws DataInvalidaException {
        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.getLucroByData(dataInicio, paginacao));
    }

}
