package br.com.dbc.wbhealth.service;

import br.com.dbc.wbhealth.exceptions.DataInvalidaException;
import br.com.dbc.wbhealth.model.dto.relatorio.RelatorioLucro;
import br.com.dbc.wbhealth.repository.AtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RequiredArgsConstructor

@Service
public class AtendimentoServiceRelatorio {

    private final AtendimentoRepository atendimentoRepository;

    public Page<RelatorioLucro> getLucroByData(String inicio, Pageable paginacao) throws DataInvalidaException {
        LocalDate dataInicio;
        try {
            dataInicio = LocalDate.parse(inicio);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data inválida");
        }
        return atendimentoRepository.getLucroByData(dataInicio, LocalDate.now(), paginacao);
    }

    public Page<RelatorioLucro> findLucroAteAgora(Pageable paginacao) {
        return atendimentoRepository.getLucroAteOMomento(LocalDate.now(), paginacao);
    }
}
