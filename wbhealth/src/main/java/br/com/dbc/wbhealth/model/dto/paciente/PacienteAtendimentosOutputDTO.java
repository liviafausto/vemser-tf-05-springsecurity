package br.com.dbc.wbhealth.model.dto.paciente;

import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PacienteAtendimentosOutputDTO {
    @Schema(description = "Identificador de paciente; associado à pessoa")
    private Integer idPaciente;

    @Schema(description = "Nome do paciente")
    private String nome;

    @Schema(description = "Atendimentos associados ao paciente")
    private List<AtendimentoOutputDTO> atendimentos;
}
