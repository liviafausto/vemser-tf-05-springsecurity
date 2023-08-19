package br.com.dbc.wbhealth.controller;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
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
public class AuthController {
    public final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioInputDTO loginDTO) throws RegraDeNegocioException{
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getSenha());

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
    public ResponseEntity<UsuarioOutputDTO> createUser(@RequestBody @Valid UsuarioInputDTO usuarioDTO)
            throws RegraDeNegocioException, EntityNotFound {
        return new ResponseEntity<>(usuarioService.create(usuarioDTO), HttpStatus.OK);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioOutputDTO> update(@PathVariable("idUsuario") Integer id,
                                             @Valid @RequestBody UsuarioInputDTO usuarioInputDTO) throws EntityNotFound {
        return new ResponseEntity<>(usuarioService.update(id, usuarioInputDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> remove(@PathVariable("idUsuario") Integer id) throws EntityNotFound {
        usuarioService.remove(id);
        return ResponseEntity.ok().build();
    }
}
