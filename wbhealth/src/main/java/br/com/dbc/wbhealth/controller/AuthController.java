package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.documentation.AuthControllerDoc;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.usuario.*;
import br.com.dbc.wbhealth.model.entity.UsuarioEntity;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.security.TokenService;
import br.com.dbc.wbhealth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthControllerDoc {
    public final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<UsuarioOutputDTO> getLoggedUser() throws RegraDeNegocioException, EntityNotFound {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginInputDTO login) throws RegraDeNegocioException{
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getLogin(), login.getSenha());

        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e){
            throw new RegraDeNegocioException("Usuário ou senha inválidos");
        }

        UsuarioEntity usuarioValidado = (UsuarioEntity) authentication.getPrincipal();
        String tokenGerado = tokenService.generateToken(usuarioValidado);

        return new ResponseEntity<>(tokenGerado, HttpStatus.OK);
    }

    @PostMapping("/create-user")
    public ResponseEntity<UsuarioOutputDTO> create(@RequestBody @Valid UsuarioInputDTO usuario)
            throws RegraDeNegocioException, EntityNotFound {
        return new ResponseEntity<>(usuarioService.create(usuario), HttpStatus.OK);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioOutputDTO> update(@PathVariable("idUsuario") Integer idUsuario,
                                                   @Valid @RequestBody UsuarioInputDTO usuario)
            throws EntityNotFound {
        return new ResponseEntity<>(usuarioService.update(idUsuario, usuario), HttpStatus.OK);
    }

    @PutMapping("update-password/{idUsuario}")
    public ResponseEntity<Void> updatePassword(@PathVariable("idUsuario") Integer idUsuario,
                                               @Valid @RequestBody UsuarioSenhaInputDTO usuario)
            throws EntityNotFound {
        usuarioService.updatePassword(idUsuario, usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> remove(@PathVariable("idUsuario") Integer idUsuario) throws EntityNotFound {
        usuarioService.remove(idUsuario);
        return ResponseEntity.ok().build();
    }

}
