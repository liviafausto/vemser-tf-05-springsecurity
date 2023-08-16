package br.com.dbc.wbhealth.model.dto.hospital;

import br.com.dbc.wbhealth.model.dto.atendimento.AtendimentoOutputDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class HospitalAtendimentoDTO {

    @Schema(description = "Identificador do Hospital")
    private Integer idHospital;

    @Schema(description = "Nome do Hospital")
    private String nome;

    @Schema(description = "Atendimentos do Hospital")
    private List<AtendimentoOutputDTO> atendimentos;
}
