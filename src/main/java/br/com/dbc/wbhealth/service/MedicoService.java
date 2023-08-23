package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.medico.*;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioInputDTO;
import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import br.com.dbc.wbhealth.model.entity.*;
import br.com.dbc.wbhealth.repository.AtendimentoRepository;
import br.com.dbc.wbhealth.repository.MedicoRepository;
import br.com.dbc.wbhealth.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final PessoaRepository pessoaRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final UsuarioService usuarioService;
    private final HospitalService hospitalService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public Page<MedicoOutputDTO> findAll(Integer pagina, Integer quantidadeRegistros){
        Sort ordenacao = Sort.by("idMedico");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        return medicoRepository.findAll(pageable).map(this::convertMedicoToOutput);
    }

    public MedicoOutputDTO findById(Integer idMedico) throws EntityNotFound {
        MedicoEntity medicoEncontrado = getMedicoById(idMedico);
        return convertMedicoToOutput(medicoEncontrado);
    }

    public MedicoNovoOutputDTO save(MedicoInputDTO medicoInputDTO) throws BancoDeDadosException, EntityNotFound, MessagingException {

        PessoaEntity pessoaEntity = convertInputToPessoa(medicoInputDTO);
        if (pessoaRepository.existsByCpf(pessoaEntity.getCpf())) {
            throw new BancoDeDadosException("CPF já cadastrado.");
        }
        if (medicoRepository.existsByCrm(medicoInputDTO.getCrm())) {
            throw new BancoDeDadosException("CRM já cadastrado.");
        }

        UsuarioInputDTO usuarioInput = usuarioService.criarUsuarioInput(medicoInputDTO.getCpf(), 4);
        UsuarioOutputDTO usuarioOutput = usuarioService.create(usuarioInput);

        PessoaEntity pessoaCriada = pessoaRepository.save(pessoaEntity);

        MedicoEntity medico = convertInputToMedico(pessoaCriada, medicoInputDTO);
        MedicoEntity medicoCriado = medicoRepository.save(medico);

        emailService.enviarEmailUsuarioCriado(medicoCriado.getPessoa(), usuarioInput, "MEDICO");

        return convertToMedicoNovoOutput(medicoCriado, usuarioOutput);
    }

    public MedicoOutputDTO update(Integer idMedico, MedicoInputDTO medicoInput) throws EntityNotFound {
        PessoaEntity pessoaModificada = convertInputToPessoa(medicoInput);
        MedicoEntity medicoModificado = convertInputToMedico(pessoaModificada, medicoInput);

        MedicoEntity medico = getMedicoById(idMedico);
        PessoaEntity pessoa = medico.getPessoa();

        pessoa.setNome(medicoModificado.getPessoa().getNome());
        pessoa.setCep(medicoModificado.getPessoa().getCep());
        pessoa.setDataNascimento(medicoModificado.getPessoa().getDataNascimento());
        pessoa.setCpf(medicoModificado.getPessoa().getCpf());
        pessoa.setSalarioMensal(medicoModificado.getPessoa().getSalarioMensal());
        pessoa.setEmail(medicoModificado.getPessoa().getEmail());

        pessoaRepository.save(medico.getPessoa());
        MedicoEntity medicoAtualizado = medicoRepository.save(medico);

        return convertMedicoToOutput(medicoAtualizado);
    }

    public void delete(Integer idMedico) throws EntityNotFound {
        MedicoEntity medico = getMedicoById(idMedico);
        Optional<UsuarioEntity> usuario = usuarioService.findByLogin(medico.getPessoa().getCpf());

        if(usuario.isPresent()){
            usuarioService.remove(usuario.get().getIdUsuario());
        }

        medicoRepository.delete(medico);
    }

    protected MedicoEntity getMedicoById(Integer idMedico) throws EntityNotFound {
        return medicoRepository.findById(idMedico)
                .orElseThrow(() -> new EntityNotFound("Médico não encontrado"));
    }

    private PessoaEntity convertInputToPessoa(MedicoInputDTO medicoInput){
        return new PessoaEntity(
                medicoInput.getNome(),
                medicoInput.getCep(),
                medicoInput.getDataNascimento(),
                medicoInput.getCpf(),
                medicoInput.getSalarioMensal(),
                medicoInput.getEmail()
        );

    }

    public MedicoEntity convertInputToMedico(PessoaEntity pessoa, MedicoInputDTO medicoInput) throws EntityNotFound {
        MedicoEntity medico = new MedicoEntity();
        medico.setPessoa(pessoa);
        medico.setCrm(medicoInput.getCrm());

        HospitalEntity hospital = hospitalService.getHospitalById(medicoInput.getIdHospital());
        medico.setHospitalEntity(hospital);

        return medico;
    }

    public void converterMedicoOutput(MedicoOutputDTO medicoOutput, PessoaEntity pessoa) {
        medicoOutput.setNome(pessoa.getNome());
        medicoOutput.setCep(pessoa.getCep());
        medicoOutput.setDataNascimento(pessoa.getDataNascimento());
        medicoOutput.setCpf(pessoa.getCpf());
        medicoOutput.setSalarioMensal(pessoa.getSalarioMensal());
        medicoOutput.setEmail(pessoa.getEmail());
        medicoOutput.setIdPessoa(pessoa.getIdPessoa());

    }

    private MedicoOutputDTO convertMedicoToOutput(MedicoEntity medico){
        MedicoOutputDTO medicoOutput = objectMapper.convertValue(medico, MedicoOutputDTO.class);

        medicoOutput.setIdHospital(medico.getHospitalEntity().getIdHospital());
        converterMedicoOutput(medicoOutput, medico.getPessoa());

        return medicoOutput;
    }

    private MedicoNovoOutputDTO convertToMedicoNovoOutput(MedicoEntity medico, UsuarioOutputDTO usuario){
        MedicoNovoOutputDTO medicoNovoOutput = objectMapper.convertValue(medico, MedicoNovoOutputDTO.class);

        medicoNovoOutput.setIdHospital(medico.getHospitalEntity().getIdHospital());
        converterMedicoOutput(medicoNovoOutput, medico.getPessoa());
        medicoNovoOutput.setUsuario(usuario);

        return medicoNovoOutput;
    }

    public List<MedicoAtendimentoDTO> generateMedicoAtendimento(Integer idMedico,
                                                                LocalDate dataInicio,
                                                                LocalDate dataFim) throws EntityNotFound {
        MedicoEntity medico = getMedicoById(idMedico);
        if(medico == null) {
            throw new EntityNotFound("Médico não encontrado com o ID: " + idMedico);
        }

        Long quantidadeAtendimentos = atendimentoRepository.countAtendimentosByMedicoAndDateRange(medico, dataInicio, dataFim);

        List<MedicoAtendimentoDTO> atendimento = new ArrayList<>();
        MedicoAtendimentoDTO medicoAtendimentoDTO = new MedicoAtendimentoDTO();
        medicoAtendimentoDTO.setNomeMedico(medico.getPessoa().getNome());
        medicoAtendimentoDTO.setCrm(medico.getCrm());
        medicoAtendimentoDTO.setQuantidadeAtendimentos(quantidadeAtendimentos);

        PeriodoDTO periodoDTO = new PeriodoDTO(dataInicio, dataFim);
        medicoAtendimentoDTO.setPeriodo(periodoDTO);

        atendimento.add(medicoAtendimentoDTO);

        return atendimento;
    }

}
