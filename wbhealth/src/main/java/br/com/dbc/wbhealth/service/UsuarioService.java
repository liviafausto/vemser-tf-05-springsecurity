package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import br.com.dbc.wbhealth.model.entity.CargoEntity;
import br.com.dbc.wbhealth.model.entity.UsuarioEntity;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.repository.CargoRepository;
import br.com.dbc.wbhealth.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CargoService cargoService;
    private final ObjectMapper objectMapper;

    public Optional<UsuarioEntity> findByLoginAndSenha(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    public UsuarioEntity findById(Integer idUsuario) throws EntityNotFound {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new EntityNotFound("Usuário não encontrado!"));
    }

    public Optional<UsuarioEntity> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public UsuarioOutputDTO create(UsuarioInputDTO usuarioDTO) throws EntityNotFound, RegraDeNegocioException {
        if(usuarioRepository.existsByLogin(usuarioDTO.getLogin())) {
            throw new RegraDeNegocioException("Nome de usuário já está em uso.");
        }

        String encodedPassword = passwordEncoder.encode(usuarioDTO.getSenha());

        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioDTO, UsuarioEntity.class);
        usuarioEntity.setSenha(encodedPassword);

        Set<CargoEntity> cargos = new HashSet<>();
        if (usuarioDTO.getCargos() != null) {
            for (Integer idCargo : usuarioDTO.getCargos()) {
                CargoEntity cargo = cargoService.findById(idCargo);
                cargos.add(cargo);
            }
            usuarioEntity.setCargos(cargos);
        }
        return convertToOutputDTO(usuarioRepository.save(usuarioEntity)) ;
    }

    public UsuarioOutputDTO convertToOutputDTO(UsuarioEntity entity) {
        UsuarioOutputDTO usuarioOutputDTO = objectMapper.convertValue(entity, UsuarioOutputDTO.class);
        Set<Integer> cargos = new HashSet<>();
        for (CargoEntity cargo : entity.getCargos()){
            cargos.add(cargo.getIdCargo());
        }
        usuarioOutputDTO.setCargos(cargos);
        return usuarioOutputDTO;
    }

}

