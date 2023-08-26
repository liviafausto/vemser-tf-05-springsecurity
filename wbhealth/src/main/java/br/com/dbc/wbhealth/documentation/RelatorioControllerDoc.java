package br.com.dbc.wbhealth.documentation;

import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.model.dto.relatorio.RelatorioLucro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface RelatorioControllerDoc {

    @Operation(
            summary = "Gerar relatório de lucro",
            description = "Gera um relatório de lucro de cada tipo"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o relatório gerado."),
                    @ApiResponse(responseCode = "400", description = "Não foi possível gerar o relatório."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @GetMapping("/lucro")
    ResponseEntity<Page<RelatorioLucro>> relatorioLucroAteOMomento(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    );

    @Operation(
            summary = "Gerar relatório de lucro entre datas",
            description = "Gera um relatório de lucro de cada tipo entre as datas"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o relatório gerado."),
                    @ApiResponse(responseCode = "400", description = "Não foi possível gerar o relatório."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @GetMapping("/lucro/data")
    ResponseEntity<Page<RelatorioLucro>> relatorioLucroPorData(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros,
            @RequestParam String dataInicio
    ) throws DataInvalidaException;

}
