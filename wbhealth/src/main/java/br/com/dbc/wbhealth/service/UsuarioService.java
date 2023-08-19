package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioSenhaInputDTO;
import br.com.dbc.wbhealth.model.entity.CargoEntity;
import br.com.dbc.wbhealth.model.entity.UsuarioEntity;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.repository.CargoRepository;
import br.com.dbc.wbhealth.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        if (usuarioRepository.existsByLogin(usuarioDTO.getLogin())) {
            throw new RegraDeNegocioException("Nome de usuário já está em uso.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getSenha());

        UsuarioEntity usuarioEntity = convertToEntity(usuarioDTO);
        usuarioEntity.setSenha(senhaCriptografada);

        return convertToOutputDTO(usuarioRepository.save(usuarioEntity));
    }

    public UsuarioOutputDTO update(Integer id, UsuarioInputDTO usuarioInputDTO) throws EntityNotFound {
        try {
            UsuarioEntity usuarioDesatualizado = findById(id);
            if (usuarioRepository.existsByLogin(usuarioInputDTO.getLogin())) {
                if (usuarioDesatualizado.getLogin() != usuarioInputDTO.getLogin()) {
                    throw new RegraDeNegocioException("Nome de usuário é utilizado por outro usuário.");
                }
            }
            UsuarioEntity entity = convertToEntity(usuarioInputDTO);
            String senhaCriptografada = passwordEncoder.encode(usuarioInputDTO.getSenha());
            entity.setSenha(senhaCriptografada);

            BeanUtils.copyProperties(entity, usuarioDesatualizado, "idUsuario");

            UsuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioDesatualizado);

            return convertToOutputDTO(usuarioAtualizado);
        } catch (RegraDeNegocioException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePassword(Integer id, UsuarioSenhaInputDTO usuarioSenhaInputDTO) throws EntityNotFound {
        UsuarioEntity usuarioParaEditar = findById(id);
        String senhaCriptografada = passwordEncoder.encode(usuarioSenhaInputDTO.getSenha());
        usuarioParaEditar.setSenha(senhaCriptografada);
        usuarioRepository.save(usuarioParaEditar);
    }

    public void remove(Integer idUsuario) throws EntityNotFound {
        UsuarioEntity usuario = findById(idUsuario);
        usuarioRepository.delete(usuario);
    }

    public UsuarioEntity convertToEntity(UsuarioInputDTO usuarioInputDTO) throws EntityNotFound {
        UsuarioEntity entity = objectMapper.convertValue(usuarioInputDTO, UsuarioEntity.class);
        Set<CargoEntity> cargos = new HashSet<>();
        if (usuarioInputDTO.getCargos() != null) {
            for (Integer idCargo : usuarioInputDTO.getCargos()) {
                CargoEntity cargo = cargoService.findById(idCargo);
                cargos.add(cargo);
            }
            entity.setCargos(cargos);
        }
        entity.setCargos(cargos);
        return entity;
    }


    public UsuarioOutputDTO convertToOutputDTO(UsuarioEntity entity) {
        UsuarioOutputDTO usuarioOutputDTO = objectMapper.convertValue(entity, UsuarioOutputDTO.class);
        Set<Integer> cargos = new HashSet<>();
        for (CargoEntity cargo : entity.getCargos()) {
            cargos.add(cargo.getIdCargo());
        }
        usuarioOutputDTO.setCargos(cargos);
        return usuarioOutputDTO;
    }

}

