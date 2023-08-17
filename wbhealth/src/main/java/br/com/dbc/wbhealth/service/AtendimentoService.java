package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.BancoDeDadosException;
import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.exceptions.EntityNotFound;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoInputDTO;
import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import br.com.dbc.wbhealth.model.entity.AtendimentoEntity;
import br.com.dbc.wbhealth.model.entity.HospitalEntity;
import br.com.dbc.wbhealth.model.entity.MedicoEntity;
import br.com.dbc.wbhealth.model.entity.PacienteEntity;
import br.com.dbc.wbhealth.model.enumarator.TipoDeAtendimento;
import br.com.dbc.wbhealth.model.enumarator.TipoEmail;
import br.com.dbc.wbhealth.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;

    private final ObjectMapper objectMapper;

    private final EmailService emailService;

    private final PacienteService pacienteService;

    private final MedicoService medicoService;

    private final HospitalService hospitalService;

    private void enviarEmails(AtendimentoEntity atendimento, TipoEmail tipo) throws MessagingException, BancoDeDadosException, EntityNotFound {
        emailService.sendEmailAtendimento(atendimento, tipo);

    }

    private void verificarIdentificadores(AtendimentoInputDTO atendimentoDeEntrada) throws BancoDeDadosException, EntityNotFound {
        Integer idPaciente = atendimentoDeEntrada.getIdPaciente();
        Integer idHospital = atendimentoDeEntrada.getIdHospital();
        Integer idMedico = atendimentoDeEntrada.getIdMedico();

        hospitalService.findById(idHospital);
        medicoService.findById(idMedico);
        pacienteService.findById(idPaciente);
    }

    public AtendimentoOutputDTO save(AtendimentoInputDTO atendimentoNovo) throws BancoDeDadosException, EntityNotFound, MessagingException {
        verificarIdentificadores(atendimentoNovo);

        AtendimentoEntity atendimento = setFKInAtendimento(atendimentoNovo);

        atendimento = atendimentoRepository.save(atendimento);

        enviarEmails(atendimento, TipoEmail.CONFIRMACAO);

        AtendimentoOutputDTO atendimentoOutputDTO = setFKInAtendimentoDTO(objectMapper.convertValue(atendimento, AtendimentoOutputDTO.class), atendimento);

        return atendimentoOutputDTO;
    }

    public List<AtendimentoOutputDTO> findAll() throws BancoDeDadosException {
        return atendimentoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public AtendimentoOutputDTO findById(Integer idAtendimento) throws BancoDeDadosException, EntityNotFound {
        AtendimentoEntity atendimento = atendimentoRepository.getById(idAtendimento);

        return convertToDTO(atendimento);
    }

    public List<AtendimentoOutputDTO> bucarAtendimentoPeloIdUsuario(Integer idPaciente) throws BancoDeDadosException {
        return findAll()
                .stream()
                .filter(atendimento -> atendimento.getIdPaciente().equals(idPaciente))
                .toList();
    }

    public AtendimentoOutputDTO update(Integer idAtendimento, AtendimentoInputDTO atendimentoAtualizado) throws BancoDeDadosException, EntityNotFound, MessagingException {
        verificarIdentificadores(atendimentoAtualizado);

        AtendimentoEntity atendimentoConvertido = setFKInAtendimento(atendimentoAtualizado);

        atendimentoConvertido.setLaudo(atendimentoConvertido.getLaudo());
        atendimentoConvertido.setTipoDeAtendimento(TipoDeAtendimento.valueOf(atendimentoAtualizado.getTipoDeAtendimento()));
        atendimentoConvertido.setDataAtendimento(atendimentoAtualizado.getDataAtendimento());

        atendimentoConvertido = atendimentoRepository.save(atendimentoConvertido);

        enviarEmails(atendimentoConvertido, TipoEmail.ATUALIZACAO);

        AtendimentoOutputDTO atendimentoOutputDTO = this.setFKInAtendimentoDTO(objectMapper.convertValue(atendimentoAtualizado, AtendimentoOutputDTO.class), atendimentoConvertido);

        return atendimentoOutputDTO;
    }

    public void deletarPeloId(Integer idAtendimento) throws EntityNotFound {
        try {
            AtendimentoInputDTO atendimento = objectMapper.convertValue(findById(idAtendimento), AtendimentoInputDTO.class);

            atendimentoRepository.deleteById(idAtendimento);

            enviarEmails(atendimentoRepository.getById(idAtendimento), TipoEmail.CANCELAMENTO);

        } catch (BancoDeDadosException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

//    private AtendimentoOutputDTO atendimentoEntityToAtendimentoOutput(AtendimentoEntity atendimento) {
//        AtendimentoOutputDTO atendimentoOutputDTO = new AtendimentoOutputDTO();
//        atendimentoOutputDTO.setIdAtendimento(atendimento.getIdAtendimento());
//        atendimentoOutputDTO.setIdHospital(atendimento.getHospitalEntity().getIdHospital());
//        atendimentoOutputDTO.setIdPaciente(atendimento.getPacienteEntity().getIdPaciente());
//        atendimentoOutputDTO.setIdMedico(atendimento.getMedicoEntity().getIdMedico());
//        atendimentoOutputDTO.setLaudo(atendimento.getLaudo());
//        atendimentoOutputDTO.setValorDoAtendimento(atendimento.getValorDoAtendimento());
//        atendimentoOutputDTO.setTipoDeAtendimento(atendimento.getTipoDeAtendimento().name());
//        atendimentoOutputDTO.setDataAtendimento(atendimento.getDataAtendimento());
//
//        return atendimentoOutputDTO;
//    }

    private AtendimentoEntity setFKInAtendimento(AtendimentoInputDTO atendimentoDTO) throws EntityNotFound {
        AtendimentoEntity atendimento = new AtendimentoEntity();

        HospitalEntity hospital = objectMapper.convertValue(hospitalService.findById(atendimentoDTO.getIdHospital()), HospitalEntity.class);

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
        return atendimentoRepository.findAll(paginacao).map(this::convertToDTO);
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
        return atendimentoRepository.findAtendimentoEntitiesByDataAtendimentoBetween(dataInicio, dataFim, paginacao).map(this::convertToDTO);
    }

    private AtendimentoOutputDTO convertToDTO(AtendimentoEntity atendimento) {
        AtendimentoOutputDTO atendimentoOutputDTO = objectMapper.convertValue(atendimento, AtendimentoOutputDTO.class);

        return atendimentoOutputDTO;
    }

    private List<AtendimentoOutputDTO> convertToDTOList(List<AtendimentoEntity> listaAtendimentos) {
        return listaAtendimentos.stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }
}
