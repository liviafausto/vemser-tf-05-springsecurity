package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutDTO;
import br.com.dbc.wbhealth.model.entity.UsuarioEntity;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public Optional<UsuarioEntity> findByLoginAndSenha(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    public Optional<UsuarioEntity> findById(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    public Optional<UsuarioEntity> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public UsuarioOutDTO create(UsuarioInputDTO usuarioDTO) throws RegraDeNegocioException {

        if(usuarioRepository.existsByLogin(usuarioDTO.getLogin())) {
            throw new RegraDeNegocioException("Nome de usuário já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(usuarioDTO.getSenha());
        usuarioDTO.setSenha(encodedPassword);
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioDTO, UsuarioEntity.class);

        UsuarioOutDTO usuarioCreated = objectMapper.convertValue(usuarioRepository.save(usuarioEntity), UsuarioOutDTO.class);

        return usuarioCreated;
    }

}

