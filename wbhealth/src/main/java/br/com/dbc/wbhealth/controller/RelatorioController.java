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

import java.time.LocalDate;

@RequiredArgsConstructor

@RestController
@RequestMapping("/relatorio")
public class RelatorioController implements RelatorioControllerDoc {

    private final AtendimentoServiceRelatorio atendimentoService;

    @GetMapping("/lucro")
    public ResponseEntity<Page<RelatorioLucro>> relatorioLucroAteOMomento(@RequestParam Integer pagina, @RequestParam Integer quantidade) {
        Pageable paginacao = PageRequest.of(pagina, quantidade);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.findLucroAteAgora(paginacao));
    }

    @GetMapping("/lucro/data")
    public ResponseEntity<Page<RelatorioLucro>> relatorioLucroPorData(@RequestParam Integer pagina, @RequestParam Integer quantidade,
                                                                      @RequestParam String dataInicio) throws DataInvalidaException {
        Pageable paginacao = PageRequest.of(pagina, quantidade);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoService.getLucroByData(dataInicio, paginacao));
    }

}
