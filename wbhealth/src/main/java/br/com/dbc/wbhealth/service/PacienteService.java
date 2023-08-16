package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteAtendimentosOutputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteInputDTO;
import br.com.dbc.wbhealth.model.dto.paciente.PacienteOutputDTO;
import br.com.dbc.wbhealth.model.entity.AtendimentoEntity;
import br.com.dbc.wbhealth.model.entity.HospitalEntity;
import br.com.dbc.wbhealth.model.entity.PacienteEntity;
import br.com.dbc.wbhealth.model.entity.PessoaEntity;
import br.com.dbc.wbhealth.repository.PacienteRepository;
import br.com.dbc.wbhealth.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final PessoaRepository pessoaRepository;
    private final HospitalService hospitalService;
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

    public PacienteOutputDTO save(PacienteInputDTO pacienteInput) throws BancoDeDadosException, EntityNotFound {
        PessoaEntity pessoa = convertInputToPessoa(pacienteInput);

        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new BancoDeDadosException("CPF já cadastrado.");
        }

        PessoaEntity pessoaCriada = pessoaRepository.save(pessoa);

        PacienteEntity paciente = convertInputToPaciente(pessoaCriada, pacienteInput);
        PacienteEntity pacienteCriado = pacienteRepository.save(paciente);

        return convertPacienteToOutput(pacienteCriado);
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
        pacienteRepository.delete(paciente);
    }

    protected PacienteEntity getPacienteById(Integer idPaciente) throws EntityNotFound {
        return pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFound("Paciente não encontrado"));
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

    private PacienteOutputDTO convertPacienteToOutput(PacienteEntity paciente){
        PacienteOutputDTO pacienteOutput = objectMapper.convertValue(paciente, PacienteOutputDTO.class);
        pacienteOutput.setIdHospital(paciente.getHospitalEntity().getIdHospital());

        PessoaEntity pessoa = paciente.getPessoa();
        pacienteOutput.setIdPessoa(pessoa.getIdPessoa());
        pacienteOutput.setNome(pessoa.getNome());
        pacienteOutput.setCep(pessoa.getCep());
        pacienteOutput.setDataNascimento(pessoa.getDataNascimento());
        pacienteOutput.setCpf(pessoa.getCpf());
        pacienteOutput.setEmail(pessoa.getEmail());

        return pacienteOutput;
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
