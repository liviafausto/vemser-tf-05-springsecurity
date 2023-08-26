package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoMedicoDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoPacienteDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalAtendimentoDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalInputDTO;
import br.com.dbc.wbhealth.model.dto.hospital.HospitalOutputDTO;
import br.com.dbc.wbhealth.model.entity.AtendimentoEntity;
import br.com.dbc.wbhealth.model.entity.HospitalEntity;
import br.com.dbc.wbhealth.repository.HospitalRepository;
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
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper;

    public List<HospitalOutputDTO> findAll() {
        List<HospitalEntity> hospitais = hospitalRepository.findAll();
        return convertListToDTO(hospitais);
    }

    public HospitalOutputDTO findById(Integer idHospital) throws EntityNotFound {
        HospitalEntity hospital = getHospitalById(idHospital);
        return convertToDTO(hospital);
    }

    public HospitalOutputDTO save(HospitalInputDTO hospitalInputDTO) {
        HospitalEntity hospital = convertToEntity(hospitalInputDTO);
        HospitalEntity hospitalCadastrado = hospitalRepository.save(hospital);
        return convertToDTO(hospitalCadastrado);
    }

    public HospitalOutputDTO update(Integer idHospital, HospitalInputDTO hospitalInputDTO) throws EntityNotFound {
        HospitalEntity hospital = getHospitalById(idHospital);
        hospital.setNome(hospitalInputDTO.getNome());

        hospitalRepository.save(hospital);
        return convertToDTO(hospital);
    }

    public void deleteById(Integer idHospital) throws EntityNotFound {
        HospitalEntity hospital = getHospitalById(idHospital);
        hospitalRepository.delete(hospital);
    }

    protected HospitalEntity getHospitalById(Integer idHospital) throws EntityNotFound {
        return hospitalRepository.findById(idHospital)
                .orElseThrow(() -> new EntityNotFound("Hospital não encontrado"));
    }

    private HospitalEntity convertToEntity(HospitalInputDTO hospitalInputDTO) {
        return objectMapper.convertValue(hospitalInputDTO, HospitalEntity.class);
    }

    private HospitalOutputDTO convertToDTO(HospitalEntity hospital) {
        return objectMapper.convertValue(hospital, HospitalOutputDTO.class);
    }

    private List<HospitalOutputDTO> convertListToDTO(List<HospitalEntity> hospitais) {
        return hospitais.stream().map(this::convertToDTO).toList();
    }

    public Page<HospitalAtendimentoDTO> findHospitaisWithAllAtendimentos(Integer pagina, Integer quantidadeRegistros) {
        Sort ordenacao = Sort.by("idHospital");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        Page<HospitalEntity> pacientesPaginados = hospitalRepository.findAll(pageable);

        return pacientesPaginados.map(this::convertToHospitalAtendimentosDTO);
    }

    private HospitalAtendimentoDTO convertToHospitalAtendimentosDTO(HospitalEntity hospital){
        HospitalAtendimentoDTO pacienteAtendimentosOutput = new HospitalAtendimentoDTO();

        pacienteAtendimentosOutput.setIdHospital(hospital.getIdHospital());
        pacienteAtendimentosOutput.setNome(hospital.getNome());

        List<AtendimentoOutputDTO> atendimentosOutput = hospital.getAtendimentos().stream()
                .map(this::convertAtendimentoToOutput).toList();
        pacienteAtendimentosOutput.setAtendimentos(atendimentosOutput);

        return pacienteAtendimentosOutput;
    }

    private AtendimentoOutputDTO convertAtendimentoToOutput(AtendimentoEntity atendimento) {
        AtendimentoOutputDTO atendimentoOutputDTO = objectMapper.convertValue(atendimento, AtendimentoOutputDTO.class);

        atendimentoOutputDTO.setIdHospital(atendimento.getHospitalEntity().getIdHospital());

        AtendimentoMedicoDTO medico = new AtendimentoMedicoDTO();
        medico.setIdMedico(atendimento.getMedicoEntity().getIdMedico());
        medico.setNomeMedico(atendimento.getMedicoEntity().getPessoa().getNome());
        atendimentoOutputDTO.setMedico(medico);

        AtendimentoPacienteDTO paciente = new AtendimentoPacienteDTO();
        paciente.setIdPaciente(atendimento.getPacienteEntity().getIdPaciente());
        paciente.setNomePaciente(atendimento.getPacienteEntity().getPessoa().getNome());
        atendimentoOutputDTO.setPaciente(paciente);

        return atendimentoOutputDTO;
    }
}
