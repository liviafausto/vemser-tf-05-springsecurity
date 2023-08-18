package br.com.dbc.wbhealth.model.dto.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoAtendimentoDTO {

    @Schema(description = "Nome do médico", required = true)
    private String nomeMedico;

    @Schema(description = "CRM do medico", example = "AM-7654321/82")
    private String crm;

    @Schema(description = "Quantidade de atendimentos dentro do período", example = "AM-7654321/82")
    private Long quantidadeAtendimentos;

    private PeriodoDTO periodo;
}
