package br.com.dbc.wbhealth.model.dto.atendimento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AtendimentoPacienteDTO {
    @Schema(description = "Id do paciente", example = "4")
    private Integer idPaciente;

    @Schema(description = "Nome do paciente")
    private String nomePaciente;
}
