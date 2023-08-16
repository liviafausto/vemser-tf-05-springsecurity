package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.medico.MedicoAtendimentoDTO;
import br.com.dbc.wbhealth.model.dto.medico.MedicoInputDTO;
import br.com.dbc.wbhealth.model.dto.medico.MedicoOutputDTO;
import br.com.dbc.wbhealth.model.dto.medico.PeriodoDTO;
import br.com.dbc.wbhealth.model.entity.HospitalEntity;
import br.com.dbc.wbhealth.model.entity.MedicoEntity;
import br.com.dbc.wbhealth.model.entity.PessoaEntity;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final PessoaRepository pessoaRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final HospitalService hospitalService;
    private final ObjectMapper objectMapper;

    public Page<MedicoOutputDTO> findAll(Integer pagina, Integer quantidadeRegistros){
        Sort ordenacao = Sort.by("idMedico");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        return medicoRepository.findAll(pageable).map(this::converterMedicoOutput);
    }

    public MedicoOutputDTO findById(Integer idMedico) throws EntityNotFound {
        MedicoEntity medicoEncontrado = getMedicoById(idMedico);
        return converterMedicoOutput(medicoEncontrado);
    }

    public MedicoOutputDTO save(MedicoInputDTO medicoInputDTO) throws BancoDeDadosException, EntityNotFound {

        PessoaEntity pessoaEntity = convertInputToPessoa(medicoInputDTO);
        if (pessoaRepository.existsByCpf(pessoaEntity.getCpf())) {
            throw new BancoDeDadosException("CPF já cadastrado.");
        }
        if (medicoRepository.existsByCrm(medicoInputDTO.getCrm())) {
            throw new BancoDeDadosException("CRM já cadastrado.");
        }

        PessoaEntity pessoaSave = pessoaRepository.save(pessoaEntity);

        MedicoEntity medico = convertInputToMedico(pessoaSave, medicoInputDTO);
        MedicoEntity medicoAtualizado = medicoRepository.save(medico);

        return converterMedicoOutput(medicoAtualizado);
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

        return converterMedicoOutput(medicoAtualizado);
    }

    public void delete(Integer idMedico) throws EntityNotFound {
        MedicoEntity medico = getMedicoById(idMedico);
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

    public MedicoOutputDTO converterMedicoOutput(MedicoEntity medico) {
        MedicoOutputDTO medicoOutput = objectMapper.convertValue(medico, MedicoOutputDTO.class);
        medicoOutput.setIdHospital(medico.getHospitalEntity().getIdHospital());

        PessoaEntity pessoa = medico.getPessoa();
        medicoOutput.setNome(pessoa.getNome());
        medicoOutput.setCep(pessoa.getCep());
        medicoOutput.setDataNascimento(pessoa.getDataNascimento());
        medicoOutput.setCpf(pessoa.getCpf());
        medicoOutput.setSalarioMensal(pessoa.getSalarioMensal());
        medicoOutput.setEmail(pessoa.getEmail());
        medicoOutput.setIdPessoa(pessoa.getIdPessoa());

        return medicoOutput;
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
