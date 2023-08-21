package br.com.dbc.wbhealth.documentation;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioLoginInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioSenhaInputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface AuthControllerDoc {
    @Operation(
            summary = "Consultar usuário autenticado",
            description = "Informa qual usuário está autenticado no momento"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os dados do usuário autenticado"),
                    @ApiResponse(responseCode = "400", description = "Nenhum usuário foi autenticado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<UsuarioOutputDTO> getLoggedUser() throws RegraDeNegocioException, EntityNotFound;

    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário através do login")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário autenticado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/login")
    ResponseEntity<String>
    login(@RequestBody @Valid UsuarioLoginInputDTO login) throws RegraDeNegocioException;

    @Operation(summary = "Criar usuário", description = "Cria um usuário com base nos dados fornecidos")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna dados do usuário criado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/create-user")
    ResponseEntity<UsuarioOutputDTO>
    create(@RequestBody @Valid UsuarioInputDTO usuario) throws RegraDeNegocioException, EntityNotFound;

    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário pelo id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os dados do usuário atualizado"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idUsuario}")
    ResponseEntity<UsuarioOutputDTO>
    update(@PathVariable("idUsuario") Integer idUsuario,
           @Valid @RequestBody UsuarioInputDTO usuario) throws EntityNotFound;


    @Operation(summary = "Atualizar senha", description = "Atualiza a senha de um usuário")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("update-password/{idUsuario}")
    ResponseEntity<Void>
    updatePassword(@PathVariable("idUsuario") Integer idUsuario,
                   @Valid @RequestBody UsuarioSenhaInputDTO usuario) throws EntityNotFound;

    @Operation(summary = "Excluir usuário", description = "Exclui o usuário associado ao id fornecido.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idUsuario}")
    ResponseEntity<Void>
    remove(@PathVariable("idUsuario") Integer id) throws EntityNotFound;
}