package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteAtendimentosOutputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteInputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteNovoOutputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteOutputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import br.com.dbc.wbhealth.model.entity.*;
import br.com.dbc.wbhealth.repository.PacienteRepository;
import br.com.dbc.wbhealth.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final PessoaRepository pessoaRepository;
    private final HospitalService hospitalService;
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public Page<PacienteOutputDTO> findAll(Integer pagina, Integer quantidadeRegistros){
        Sort ordenacao = Sort.by("idPaciente");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);

        return pacienteRepository.findAll(pageable).map(this::convertPacienteToOutput);
    }

    public Page<PacienteAtendimentosOutputDTO> findAllAtendimentos(Integer pagina, Integer quantidadeRegistros) {
        Sort ordenacao = Sort.by("idPaciente");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        Page<PacienteEntity> pacientesPaginados = pacienteRepository.findAll(pageable);

        return pacientesPaginados.map(this::convertToPacienteAtendimentosOutput);
    }

    public PacienteOutputDTO findById(Integer idPaciente) throws EntityNotFound {
        PacienteEntity pacienteEncontrado = getPacienteById(idPaciente);
        return convertPacienteToOutput(pacienteEncontrado);
    }

    public PacienteNovoOutputDTO save(PacienteInputDTO pacienteInput) throws BancoDeDadosException, EntityNotFound, MessagingException {
        PessoaEntity pessoa = convertInputToPessoa(pacienteInput);

        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new BancoDeDadosException("CPF já cadastrado.");
        }

        UsuarioInputDTO usuarioInput = usuarioService.criarUsuarioInput(pacienteInput.getCpf(), 3);
        UsuarioOutputDTO usuarioOutput = usuarioService.create(usuarioInput);

        PessoaEntity pessoaCriada = pessoaRepository.save(pessoa);

        PacienteEntity paciente = convertInputToPaciente(pessoaCriada, pacienteInput);
        PacienteEntity pacienteCriado = pacienteRepository.save(paciente);

        emailService.enviarEmailUsuarioCriado(pacienteCriado.getPessoa(), usuarioInput, "PACIENTE");
        return convertToPacienteNovoOutput(pacienteCriado, usuarioOutput);
    }

    public PacienteOutputDTO update(Integer idPaciente, PacienteInputDTO pacienteInput) throws EntityNotFound {
        PessoaEntity pessoaModificada = convertInputToPessoa(pacienteInput);
        PacienteEntity pacienteModificado = convertInputToPaciente(pessoaModificada, pacienteInput);

        PacienteEntity paciente = getPacienteById(idPaciente);
        PessoaEntity pessoa = paciente.getPessoa();

        pessoa.setNome(pacienteModificado.getPessoa().getNome());
        pessoa.setCep(pacienteModificado.getPessoa().getCep());
        pessoa.setDataNascimento(pacienteModificado.getPessoa().getDataNascimento());
        pessoa.setCpf(pacienteModificado.getPessoa().getCpf());
        pessoa.setEmail(pacienteModificado.getPessoa().getEmail());

        pessoaRepository.save(paciente.getPessoa());
        PacienteEntity pacienteAtualizado = pacienteRepository.save(paciente);
        return convertPacienteToOutput(pacienteAtualizado);
    }

    public void delete(Integer idPaciente) throws EntityNotFound {
        PacienteEntity paciente = getPacienteById(idPaciente);
        Optional<UsuarioEntity> usuario = usuarioService.findByLogin(paciente.getPessoa().getCpf());

        if(usuario.isPresent()){
            usuarioService.remove(usuario.get().getIdUsuario());
        }

        pacienteRepository.delete(paciente);
    }

    protected PacienteEntity getPacienteById(Integer idPaciente) throws EntityNotFound {
        return pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFound("Paciente não encontrado"));
    }

    private UsuarioInputDTO criarUsuarioInputParaPaciente(String loginPaciente){
        UsuarioInputDTO usuarioInput = new UsuarioInputDTO();

        usuarioInput.setLogin(loginPaciente);
        usuarioInput.setSenha(usuarioService.generateRandomPassword());
        usuarioInput.setCargos(new HashSet<>());
        usuarioInput.getCargos().add(3);

        return usuarioInput;
    }

    private PessoaEntity convertInputToPessoa(PacienteInputDTO pacienteInput){
        PessoaEntity pessoa = new PessoaEntity();

        pessoa.setNome(pacienteInput.getNome());
        pessoa.setCep(pacienteInput.getCep());
        pessoa.setDataNascimento(pacienteInput.getDataNascimento());
        pessoa.setCpf(pacienteInput.getCpf());
        pessoa.setEmail(pacienteInput.getEmail());

        return pessoa;
    }

    private PacienteEntity convertInputToPaciente(PessoaEntity pessoa, PacienteInputDTO pacienteInput)
            throws EntityNotFound {
        PacienteEntity paciente = new PacienteEntity();
        paciente.setPessoa(pessoa);

        HospitalEntity hospital = hospitalService.getHospitalById(pacienteInput.getIdHospital());
        paciente.setHospitalEntity(hospital);

        return paciente;
    }

    private void convertPessoaToOutput(PacienteOutputDTO pacienteOutput, PessoaEntity pessoa){
        pacienteOutput.setIdPessoa(pessoa.getIdPessoa());
        pacienteOutput.setNome(pessoa.getNome());
        pacienteOutput.setCep(pessoa.getCep());
        pacienteOutput.setDataNascimento(pessoa.getDataNascimento());
        pacienteOutput.setCpf(pessoa.getCpf());
        pacienteOutput.setEmail(pessoa.getEmail());
    }

    private PacienteOutputDTO convertPacienteToOutput(PacienteEntity paciente){
        PacienteOutputDTO pacienteOutput = objectMapper.convertValue(paciente, PacienteOutputDTO.class);

        pacienteOutput.setIdHospital(paciente.getHospitalEntity().getIdHospital());
        convertPessoaToOutput(pacienteOutput, paciente.getPessoa());

        return pacienteOutput;
    }

    private PacienteNovoOutputDTO convertToPacienteNovoOutput(PacienteEntity paciente, UsuarioOutputDTO usuario){
        PacienteNovoOutputDTO pacienteNovoOutput = objectMapper.convertValue(paciente, PacienteNovoOutputDTO.class);

        pacienteNovoOutput.setIdHospital(paciente.getHospitalEntity().getIdHospital());
        convertPessoaToOutput(pacienteNovoOutput, paciente.getPessoa());
        pacienteNovoOutput.setUsuario(usuario);

        return pacienteNovoOutput;
    }

    private PacienteAtendimentosOutputDTO convertToPacienteAtendimentosOutput(PacienteEntity paciente){
        PacienteAtendimentosOutputDTO pacienteAtendimentosOutput = new PacienteAtendimentosOutputDTO();

        pacienteAtendimentosOutput.setIdPaciente(paciente.getIdPaciente());
        pacienteAtendimentosOutput.setNome(paciente.getPessoa().getNome());

        List<AtendimentoOutputDTO> atendimentosOutput = paciente.getAtendimentos().stream()
                .map(this::convertAtendimentoToOutput).toList();
        pacienteAtendimentosOutput.setAtendimentos(atendimentosOutput);

        return pacienteAtendimentosOutput;
    }

    private AtendimentoOutputDTO convertAtendimentoToOutput(AtendimentoEntity atendimento) {
        AtendimentoOutputDTO atendimentoOutputDTO = new AtendimentoOutputDTO();
        atendimentoOutputDTO.setIdAtendimento(atendimento.getIdAtendimento());
        atendimentoOutputDTO.setIdHospital(atendimento.getHospitalEntity().getIdHospital());
        atendimentoOutputDTO.setIdPaciente(atendimento.getPacienteEntity().getIdPaciente());
        atendimentoOutputDTO.setIdMedico(atendimento.getMedicoEntity().getIdMedico());
        atendimentoOutputDTO.setLaudo(atendimento.getLaudo());
        atendimentoOutputDTO.setValorDoAtendimento(atendimento.getValorDoAtendimento());
        atendimentoOutputDTO.setTipoDeAtendimento(atendimento.getTipoDeAtendimento().name());
        atendimentoOutputDTO.setDataAtendimento(atendimento.getDataAtendimento());

        return atendimentoOutputDTO;
    }
}
