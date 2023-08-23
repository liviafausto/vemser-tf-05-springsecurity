package br.com.dbc.wbhealth.model.dto.atendimento;

import br.com.dbc.wbhealth.model.entity.AtendimentoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AtendimentoOutputDTO {

    @Schema(description = "ID do atendimento.", example = "3", required = true)
    private Integer idAtendimento;

    @Schema(description = "Id do hospital de atendimento", example = "2", required = true)
    private Integer idHospital;

    @Schema(description = "Id do paciente", example = "4", required = true)
    private Integer idPaciente;

    @Schema(description = "Id do médico", example = "1", required = true)
    private Integer idMedico;

    @Schema(description = "Data de atendimento", example = "30/12/2099", required = true)
    private LocalDate dataAtendimento;

    @Schema(description = "Laudo do atendimento", example = "Dor de cabeça", required = true)
    private String laudo;

    @Schema(description = "Tipo do atendimento", example = "CONSULTA", required = true)
    private String tipoDeAtendimento;

    @Schema(description = "Receita médica", example = "Dipirona", required = true)
    private String receita;

    @Schema(description = "Valor do atendimento", example = "200", required = true)
    private Double valorDoAtendimento;

    public AtendimentoOutputDTO(AtendimentoEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
