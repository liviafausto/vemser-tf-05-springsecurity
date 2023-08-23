package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoInputDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.model.entity.AtendimentoEntity;
import br.com.dbc.wbhealth.model.entity.HospitalEntity;
import br.com.dbc.wbhealth.model.entity.MedicoEntity;
import br.com.dbc.wbhealth.model.entity.PacienteEntity;
import br.com.dbc.wbhealth.model.enumarator.TipoDeAtendimento;
import br.com.dbc.wbhealth.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    private final ObjectMapper objectMapper;

    private final PacienteService pacienteService;

    private final MedicoService medicoService;

    private final HospitalService hospitalService;


    private void verificarIdentificadores(AtendimentoInputDTO atendimentoDeEntrada) throws EntityNotFound {
        Integer idPaciente = atendimentoDeEntrada.getIdPaciente();
        Integer idHospital = atendimentoDeEntrada.getIdHospital();
        Integer idMedico = atendimentoDeEntrada.getIdMedico();

        hospitalService.findById(idHospital);
        medicoService.findById(idMedico);
        pacienteService.findById(idPaciente);
    }

    public AtendimentoOutputDTO save(AtendimentoInputDTO atendimentoNovo) throws EntityNotFound {
        verificarIdentificadores(atendimentoNovo);
        AtendimentoEntity atendimento = convertInputToEntityWithoutID(atendimentoNovo);
        atendimento = atendimentoRepository.save(atendimento);

        return convertToOutputDTO(atendimento);
    }

    public List<AtendimentoOutputDTO> findAll() {
        return convertToDTOList(atendimentoRepository.findAll());
    }

    public AtendimentoOutputDTO findById(Integer idAtendimento) throws EntityNotFound {
        AtendimentoEntity atendimentoEntity = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new EntityNotFound("Atendimento não encontrado"));
        return convertToOutputDTO(atendimentoEntity);
    }

    public List<AtendimentoOutputDTO> bucarAtendimentoPeloIdUsuario(Integer idPaciente) throws EntityNotFound {
        pacienteService.findById(idPaciente);
        return findAll()
                .stream()
                .filter(atendimento -> atendimento.getIdPaciente().equals(idPaciente))
                .toList();
    }

    public AtendimentoOutputDTO update(Integer idAtendimento, AtendimentoInputDTO atendimentoAtualizado) throws EntityNotFound {
        findById(idAtendimento);
        verificarIdentificadores(atendimentoAtualizado);

        AtendimentoEntity atendimentoConvertido = convertInputToEntityWithoutID(atendimentoAtualizado);
        atendimentoConvertido.setIdAtendimento(idAtendimento);

        atendimentoConvertido = atendimentoRepository.save(atendimentoConvertido);

        return convertToOutputDTO(atendimentoConvertido);
    }

    public void deletarPeloId(Integer idAtendimento) throws EntityNotFound {
        findById(idAtendimento);
        atendimentoRepository.deleteById(idAtendimento);
    }

    private AtendimentoEntity convertInputToEntityWithoutID(AtendimentoInputDTO atendimentoDTO) throws EntityNotFound {
        AtendimentoEntity atendimento = new AtendimentoEntity();

        HospitalEntity hospital = hospitalService.getHospitalById(atendimentoDTO.getIdHospital());
        MedicoEntity medico = medicoService.getMedicoById(atendimentoDTO.getIdMedico());
        PacienteEntity paciente = pacienteService.getPacienteById(atendimentoDTO.getIdPaciente());

        atendimento.setPacienteEntity(paciente);
        atendimento.setMedicoEntity(medico);
        atendimento.setHospitalEntity(hospital);

        atendimento.setLaudo(atendimentoDTO.getLaudo());
        atendimento.setValorDoAtendimento(atendimentoDTO.getValorDoAtendimento());
        atendimento.setTipoDeAtendimento(TipoDeAtendimento.valueOf(atendimentoDTO.getTipoDeAtendimento()));
        atendimento.setDataAtendimento(atendimentoDTO.getDataAtendimento());

        return atendimento;
    }

    private AtendimentoOutputDTO setFKInAtendimentoDTO(AtendimentoOutputDTO atendimentoOutputDTO, AtendimentoEntity atendimento) {
        atendimentoOutputDTO.setIdHospital(atendimento.getHospitalEntity().getIdHospital());
        atendimentoOutputDTO.setIdMedico(atendimento.getMedicoEntity().getIdMedico());
        atendimentoOutputDTO.setIdPaciente(atendimento.getPacienteEntity().getIdPaciente());
        return atendimentoOutputDTO;
    }

    public Page<AtendimentoOutputDTO> findAllPaginada(Integer pagina, Integer quantidadeRegistros) {
        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros);
        return atendimentoRepository.findAll(paginacao).map(this::convertToOutputDTO);
    }

    public Page<AtendimentoOutputDTO> findAllPaginadaByData(String inicio,
                                                            String fim,
                                                            Integer pagina,
                                                            Integer quantidadeRegistros) throws DataInvalidaException {
        LocalDate dataInicio;
        LocalDate dataFim;
        try {
            dataInicio = LocalDate.parse(inicio);
            dataFim = LocalDate.parse(fim);
        } catch (Exception e) {
            throw new DataInvalidaException("Data inválida!");
        }

        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros);
        return atendimentoRepository.findAtendimentoEntitiesByDataAtendimentoBetween(dataInicio, dataFim, paginacao).map(this::convertToOutputDTO);
    }

    private AtendimentoOutputDTO convertToOutputDTO(AtendimentoEntity entity) {
        AtendimentoOutputDTO atendimentoOutputDTO = objectMapper.convertValue(entity, AtendimentoOutputDTO.class);
        return setFKInAtendimentoDTO(atendimentoOutputDTO, entity);
    }

    private List<AtendimentoOutputDTO> convertToDTOList(List<AtendimentoEntity> listaAtendimentos) {
        return listaAtendimentos.stream()
                .map(this::convertToOutputDTO).collect(Collectors.toList());
    }
}
