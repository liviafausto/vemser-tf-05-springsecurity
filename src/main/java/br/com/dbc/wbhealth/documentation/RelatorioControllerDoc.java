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

public interface RelatorioControllerDoc {

    @Operation(summary = "Gerar relatório de lúcro até agora.", description = "Gera um relatório de lucro de cada tipo.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o relatório gerado."),
                    @ApiResponse(responseCode = "400", description = "Não foi possível gerar o relatório."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @GetMapping("/lucro")
    ResponseEntity<Page<RelatorioLucro>> relatorioLucroAteOMomento(@RequestParam Integer pagina, @RequestParam Integer quantidade);

    @Operation(summary = "Gerar relatório de lúcro entre um intervalo de datas.", description = "Gera um relatório de lucro de cada tipo entre as datas.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o relatório gerado."),
                    @ApiResponse(responseCode = "400", description = "Não foi possível gerar o relatório."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @GetMapping("/lucro/data")
    ResponseEntity<Page<RelatorioLucro>> relatorioLucroPorData(@RequestParam Integer pagina, @RequestParam Integer quantidade,
                                                                      @RequestParam String dataInicio) throws DataInvalidaException;

}
