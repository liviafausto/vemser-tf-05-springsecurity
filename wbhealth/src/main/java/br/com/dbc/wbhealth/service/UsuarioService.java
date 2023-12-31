package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.exceptions.RegraDeNegocioException;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioSenhaInputDTO;
import br.com.dbc.wbhealth.model.entity.CargoEntity;
import br.com.dbc.wbhealth.model.entity.UsuarioEntity;
import br.com.dbc.wbhealth.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CargoService cargoService;
    private final ObjectMapper objectMapper;

    public UsuarioEntity findById(Integer idUsuario) throws EntityNotFound {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFound("Usuário não encontrado"));
    }

    public Optional<UsuarioEntity> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public Integer getIdLoggedUser() throws RegraDeNegocioException {
        String idEmString = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer idUsuario;

        try {
            idUsuario = Integer.parseInt(idEmString);
        } catch (NumberFormatException e) {
            throw new RegraDeNegocioException("Não existe nenhum usuário logado");
        }

        return idUsuario;
    }

    public UsuarioOutputDTO getLoggedUser() throws RegraDeNegocioException, EntityNotFound {
        UsuarioEntity usuario = findById(getIdLoggedUser());
        return convertUsuarioToOutput(usuario);
    }

    public UsuarioOutputDTO create(UsuarioInputDTO usuarioInput) throws EntityNotFound, RegraDeNegocioException {
        if (usuarioRepository.existsByLogin(usuarioInput.getLogin())) {
            throw new RegraDeNegocioException("Nome de usuário já está em uso.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioInput.getSenha());

        UsuarioEntity usuarioEntity = convertInputToUsuario(usuarioInput);
        usuarioEntity.setSenha(senhaCriptografada);

        return convertUsuarioToOutput(usuarioRepository.save(usuarioEntity));
    }

    public UsuarioOutputDTO update(Integer idUsuario, UsuarioInputDTO usuarioInput) throws EntityNotFound, RegraDeNegocioException {
        UsuarioEntity usuarioDesatualizado = findById(idUsuario);

        boolean loginSeraAlterado = !(usuarioInput.getLogin().equals(usuarioDesatualizado.getLogin()));
        boolean novoLoginJaExiste = usuarioRepository.existsByLogin(usuarioInput.getLogin());

        if (loginSeraAlterado && novoLoginJaExiste) {
            throw new RegraDeNegocioException("Nome de usuário é utilizado por outro usuário.");
        }

        UsuarioEntity entity = convertInputToUsuario(usuarioInput);
        String senhaCriptografada = passwordEncoder.encode(usuarioInput.getSenha());
        entity.setSenha(senhaCriptografada);

        BeanUtils.copyProperties(entity, usuarioDesatualizado, "idUsuario");

        UsuarioEntity usuarioAtualizado = usuarioRepository.save(usuarioDesatualizado);

        return convertUsuarioToOutput(usuarioAtualizado);
    }

    public void updatePassword(UsuarioSenhaInputDTO usuarioSenhaInput) throws EntityNotFound, RegraDeNegocioException {
        UsuarioEntity usuarioParaEditar = findById(getIdLoggedUser());
        String senhaCriptografada = passwordEncoder.encode(usuarioSenhaInput.getSenha());
        usuarioParaEditar.setSenha(senhaCriptografada);
        usuarioRepository.save(usuarioParaEditar);
    }

    public void remove(Integer idUsuario) throws EntityNotFound {
        UsuarioEntity usuario = findById(idUsuario);
        usuarioRepository.delete(usuario);
    }

    public UsuarioEntity convertInputToUsuario(UsuarioInputDTO usuarioInputDTO) throws EntityNotFound {
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

    public UsuarioOutputDTO convertUsuarioToOutput(UsuarioEntity entity) {
        UsuarioOutputDTO usuarioOutputDTO = objectMapper.convertValue(entity, UsuarioOutputDTO.class);
        Set<Integer> cargos = new HashSet<>();
        for (CargoEntity cargo : entity.getCargos()) {
            cargos.add(cargo.getIdCargo());
        }
        usuarioOutputDTO.setCargos(cargos);
        return usuarioOutputDTO;
    }

    public String generateRandomPassword() {
        String senhaAleatoria = "";
        String charMaiusculo = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String charMinusculo = "abcdefghijklmnopqrstuvwxyz";
        String digito = "0123456789";
        String caracteres = charMaiusculo + charMinusculo + digito;

        senhaAleatoria += charMaiusculo.charAt((int) Math.floor(Math.random() * charMaiusculo.length()));
        senhaAleatoria += charMinusculo.charAt((int) Math.floor(Math.random() * charMinusculo.length()));
        senhaAleatoria += digito.charAt((int) Math.floor(Math.random() * digito.length()));

        for (int i = 0; i <= 3; i++) {
            senhaAleatoria += caracteres.charAt((int) Math.floor(Math.random() * caracteres.length()));
        }

        char[] senhaArray = senhaAleatoria.toCharArray();
        for (int i = senhaArray.length - 1; i > 0; i--) {
            int j = (int) Math.floor(Math.random() * (i + 1));
            char temp = senhaArray[i];
            senhaArray[i] = senhaArray[j];
            senhaArray[j] = temp;
        }

        senhaAleatoria = new String(senhaArray);

        return senhaAleatoria;

    }

    protected UsuarioInputDTO criarUsuarioInput(String login, Integer cargo) {
        UsuarioInputDTO usuarioInput = new UsuarioInputDTO();

        usuarioInput.setLogin(login);
        usuarioInput.setSenha(generateRandomPassword());
        usuarioInput.setCargos(new HashSet<>());
        usuarioInput.getCargos().add(cargo);

        return usuarioInput;
    }

}

