package br.com.dbc.wbhealth.documentation;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalAtendimentoDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalInputDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalOutputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface HospitalControllerDoc {

    @Operation(summary = "Listar hospitais", description = "Lista  todos os hospitais do banco")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista gerada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )

    @GetMapping
    ResponseEntity<List<HospitalOutputDTO>>
    findAll();

    @Operation(summary = "Buscar hospital pelo id", description = "Busca um hospital pelo o seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Busca do hospital realizada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idHospital}")
    ResponseEntity<HospitalOutputDTO>
    findById(@Positive @PathVariable Integer idHospital) throws EntityNotFound;

    @Operation(
            summary = "Listar Hospitais com Atendimentos",
            description = "Lista todos os hospitais com todos os atendimentos forma paginada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar o hospital"),
                    @ApiResponse(responseCode = "404", description = "Hospital não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/atendimentos")
    ResponseEntity<Page<HospitalAtendimentoDTO>>
    findHospitaisWithAllAtendimentos(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    );

    @Operation(summary = "Adicionar hospital", description = "Cria hospital com o dado repassado pela requisicao")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso!"),
                    @ApiResponse(responseCode = "400", description = "Problema encontrado nos dados da requisição."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<HospitalOutputDTO>
    save(@Valid @RequestBody HospitalInputDTO hospital) throws BancoDeDadosException;

    @Operation(summary = "Alterar hospital", description = "Altera hospital com dados que sao repassados pela requisicao")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso!"),
                    @ApiResponse(responseCode = "400", description = "Problema encontrado nos dados da requisição."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idHospital}")
    ResponseEntity<HospitalOutputDTO>
    update(@Positive @PathVariable Integer idHospital,
           @Valid @RequestBody HospitalInputDTO hospital) throws EntityNotFound, BancoDeDadosException;

    @Operation(summary = "Deletar hospital", description = "Deleta o hospital pelo o seu id especifico")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Excluido com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idHospital}")
    ResponseEntity<Void>
    deleteById(@Positive @PathVariable Integer idHospital) throws EntityNotFound;
}
