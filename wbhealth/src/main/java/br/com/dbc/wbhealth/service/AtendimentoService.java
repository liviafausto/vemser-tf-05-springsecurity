package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.*;
import br.com.dbc.wbhealth.model.entity.*;
import br.com.dbc.wbhealth.model.enumarator.TipoDeAtendimento;
import br.com.dbc.wbhealth.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<AtendimentoOutputDTO> findAllPaginada(Integer pagina, Integer quantidadeRegistros) {
        Sort ordenacao = Sort.by("idAtendimento");
        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        return atendimentoRepository.findAll(paginacao).map(this::convertToOutputDTO);
    }

    public Page<AtendimentoOutputDTO> findAllPaginadaByData(
            String inicio, String fim, Integer pagina, Integer quantidadeRegistros
    ) throws DataInvalidaException {
        LocalDate dataInicio, dataFim;

        try {
            dataInicio = LocalDate.parse(inicio);
            dataFim = LocalDate.parse(fim);
        } catch (Exception e) {
            throw new DataInvalidaException("Data inválida!");
        }

        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros);
        return atendimentoRepository
                .findAtendimentoEntitiesByDataAtendimentoBetween(dataInicio, dataFim, paginacao)
                .map(this::convertToOutputDTO);
    }

    public AtendimentoOutputDTO findById(Integer idAtendimento) throws EntityNotFound {
        AtendimentoEntity atendimentoEntity = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new EntityNotFound("Atendimento não encontrado"));
        return convertToOutputDTO(atendimentoEntity);
    }

    public Page<AtendimentoOutputDTO> bucarAtendimentoPeloCpfPaciente(
            String cpfPaciente, Integer pagina, Integer quantidadeRegistros
    ) throws EntityNotFound {
        PacienteEntity pacienteSolicitado = pacienteService.findByCpf(cpfPaciente);

        Sort ordenacao = Sort.by(Sort.Direction.DESC, "dataAtendimento");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        Page<AtendimentoEntity> atendimentosDoPaciente =
                atendimentoRepository.findByPacienteEntity(pacienteSolicitado, pageable);

        if(atendimentosDoPaciente.isEmpty())
            throw new EntityNotFound("Nenhum atendimento foi encontrado para esse paciente");

        return atendimentosDoPaciente.map(this::convertToOutputDTO);
    }

    public Page<AtendimentoOutputDTO> buscarAtendimentoPeloCpfMedico (
            String cpfMedico, Integer pagina, Integer quantidadeRegistros
    ) throws EntityNotFound {
        MedicoEntity medicoSolicitado = medicoService.findByCpf(cpfMedico);

        Sort ordenacao = Sort.by(Sort.Direction.DESC, "dataAtendimento");
        Pageable paginacao = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        Page<AtendimentoEntity> atendimentosDoMedico =
                atendimentoRepository.findByMedicoEntity(medicoSolicitado, paginacao);

        if(atendimentosDoMedico.isEmpty())
            throw new EntityNotFound("Nenhum atendimento foi encontrado para esse médico");

        return atendimentosDoMedico.map(this::convertToOutputDTO);
    }

    public AtendimentoOutputDTO save(AtendimentoInputDTO atendimentoNovo) throws EntityNotFound {
        verificarIdentificadores(atendimentoNovo);
        AtendimentoEntity atendimento = convertInputToEntityWithoutID(atendimentoNovo);
        atendimento = atendimentoRepository.save(atendimento);

        return convertToOutputDTO(atendimento);
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

    private void verificarIdentificadores(AtendimentoInputDTO atendimentoDeEntrada) throws EntityNotFound {
        Integer idPaciente = atendimentoDeEntrada.getIdPaciente();
        Integer idHospital = atendimentoDeEntrada.getIdHospital();
        Integer idMedico = atendimentoDeEntrada.getIdMedico();

        hospitalService.findById(idHospital);
        medicoService.findById(idMedico);
        pacienteService.findById(idPaciente);
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
        atendimento.setReceita(atendimentoDTO.getReceita());
        atendimento.setDataAtendimento(atendimentoDTO.getDataAtendimento());

        return atendimento;
    }

    private AtendimentoOutputDTO convertToOutputDTO(AtendimentoEntity atendimento) {
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

    private List<AtendimentoOutputDTO> convertToDTOList(List<AtendimentoEntity> listaAtendimentos) {
        return listaAtendimentos.stream().map(this::convertToOutputDTO).collect(Collectors.toList());
    }

}
