package br.com.dbc.wbhealth.documentation;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoInputDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface AtendimentoControllerDoc {
    @Operation(
            summary = "Listar atendimentos",
            description = "Lista todos os atendimentos do banco de forma paginada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os atendimentos paginados"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar os atendimentos"),
                    @ApiResponse(responseCode = "404", description = "Nenhum atendimento foi encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<Page<AtendimentoOutputDTO>> findAllPaginada(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    );

    @Operation(
            summary = "Listar atendimentos entre datas",
            description = "Lista todos os atendimentos entre datas específicas de forma paginada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os atendimentos paginados"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar os atendimentos"),
                    @ApiResponse(responseCode = "404", description = "Nenhum atendimento foi encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/data")
    ResponseEntity<Page<AtendimentoOutputDTO>> findAllPaginadaByData(
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros,
            @RequestParam String dataInicio,
            @RequestParam String dataFinal
    ) throws DataInvalidaException;

    @Operation(
            summary = "Buscar atendimento por id",
            description = "Busca o atendimento associado ao id fornecido"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o atendimento encontrado"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar o atendimento"),
                    @ApiResponse(responseCode = "404", description = "Atendimento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idAtendimento}")
    ResponseEntity<AtendimentoOutputDTO> buscarAtendimentoPeloId(
            @Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento
    ) throws BancoDeDadosException, EntityNotFound;

    @Operation(
            summary = "Buscar atendimentos do paciente por CPF",
            description = "Busca os atendimento do paciente associado ao CPF fornecido, de forma paginada\n"
                    + "Os atendimentos são listados em ordem decrescente de data "
                    + "- ou seja, da mais recente para a mais antiga"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os atendimentos relacionados ao paciente"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar os atendimentos"),
                    @ApiResponse(responseCode = "404", description = "Nenhum atendimento foi encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/paciente/{cpfPaciente}")
    ResponseEntity<Page<AtendimentoOutputDTO>> bucarAtendimentoPeloCpfPaciente(
            @CPF @PathVariable String cpfPaciente,
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) throws BancoDeDadosException, EntityNotFound;

    @Operation(
            summary = "Buscar atendimentos do médico por CPF",
            description = "Busca os atendimentos do médico associado ao CPF fornecido, de forma paginada\n"
                    + "Os atendimentos são listados em ordem decrescente de data "
                    + "- ou seja, da mais recente para a mais antiga"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os atendimentos relacionados ao médico"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar os atendimentos"),
                    @ApiResponse(responseCode = "404", description = "Nenhum atendimento foi encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/medico/{cpfMedico}")
    ResponseEntity<Page<AtendimentoOutputDTO>> bucarAtendimentoPeloCpfMedico(
            @CPF @PathVariable String cpfMedico,
            @RequestParam(name = "pagina", defaultValue = "0") @PositiveOrZero Integer pagina,
            @RequestParam(name = "quantidadeRegistros", defaultValue = "5") @Positive Integer quantidadeRegistros
    ) throws EntityNotFound;

    @Operation(
            summary = "Cadastrar atendimento",
            description = "Cadastra um novo atendimento no banco de dados"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o atendimento criado"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível registar o atendimento no banco"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<AtendimentoOutputDTO> save(
            @Valid @RequestBody AtendimentoInputDTO novoAtendimento
    ) throws BancoDeDadosException, EntityNotFound, MessagingException;

    @Operation(
            summary = "Atualizar atendimento",
            description = "Altera informações do atendimento associado ao id fornecido"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o atendimento atualizado"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar o atendimento"),
                    @ApiResponse(responseCode = "404", description = "Nenhum atendimento foi encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idAtendimento}")
    ResponseEntity<AtendimentoOutputDTO> alterarPeloId(
            @Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento,
            @Valid @RequestBody AtendimentoInputDTO atendimento
    ) throws BancoDeDadosException, EntityNotFound, MessagingException;

    @Operation(
            summary = "Deletar atendimento",
            description = "Deleta o atendimento associado ao id fornecido"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "O atendimento foi removido do banco de dados"),
                    @ApiResponse(responseCode = "400", description = "Não foi possível buscar o atendimento"),
                    @ApiResponse(responseCode = "404", description = "Nenhum atendimento foi encontrado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idAtendimento}")
    ResponseEntity<Void> deletarAtendimento(
            @Positive(message = "Deve ser positivo") @PathVariable Integer idAtendimento
    ) throws EntityNotFound;

}
