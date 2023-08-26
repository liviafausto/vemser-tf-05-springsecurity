package br.com.dbc.wbhealth.model.dto.atendimento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AtendimentoMedicoDTO {
    @Schema(description = "Id do médico", example = "1")
    private Integer idMedico;

    @Schema(description = "Nome do médico")
    private String nomeMedico;
}
