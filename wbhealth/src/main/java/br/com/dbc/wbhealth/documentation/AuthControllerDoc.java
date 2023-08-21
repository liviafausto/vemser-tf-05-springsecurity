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

    @Operation(summary = "Autenticar um usuário.", description = "Autenticar um usuário.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Autenticar um usuário."),
                    @ApiResponse(responseCode = "4034", description = "Usuário não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginInputDTO loginDTO) throws RegraDeNegocioException;

    @Operation(summary = "Criar um usuário.", description = "Criar um usuário.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Criar um usuário."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @PostMapping("/create-user")
    ResponseEntity<UsuarioOutputDTO> createUser(@RequestBody @Valid UsuarioInputDTO usuarioDTO)
            throws RegraDeNegocioException, EntityNotFound;

    @Operation(summary = "Criar um usuário.", description = "Criar um usuário.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Criar um usuário."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @PostMapping("/create-user")
    @PutMapping("/{idUsuario}")
    ResponseEntity<UsuarioOutputDTO> update(@PathVariable("idUsuario") Integer id,
                                            @Valid @RequestBody UsuarioInputDTO usuarioInputDTO) throws EntityNotFound;

    @Operation(summary = "Deletar um usuário pelo ID.", description = "Deletar um usuário pelo ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deletar um usuário pelo ID."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @PostMapping("/create-user")
    @DeleteMapping("/{idUsuario}")
    ResponseEntity<Void> remove(@PathVariable("idUsuario") Integer id) throws EntityNotFound;


    @Operation(summary = "Atualizar um usuário pelo ID.", description = "Atualizar um usuário pelo ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Atualizar um usuário pelo ID."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso."),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção.")
            }
    )
    @PostMapping("/create-user")
    @PutMapping("update-password/{idUsuario}")
    ResponseEntity<UsuarioOutputDTO> updatePassword(@PathVariable("idUsuario") Integer id,
                                                    @Valid @RequestBody UsuarioSenhaInputDTO usuarioSenhaInputDTO) throws EntityNotFound;
}