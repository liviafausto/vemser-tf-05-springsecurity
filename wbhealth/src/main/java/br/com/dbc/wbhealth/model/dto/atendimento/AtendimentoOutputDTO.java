package br.com.dbc.wbhealth.model.dto.atendimento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtendimentoOutputDTO {

    @Schema(description = "Id do atendimento.", example = "3")
    private Integer idAtendimento;

    @Schema(description = "Id do hospital de atendimento", example = "2")
    private Integer idHospital;

    @Schema(description = "Data de atendimento", example = "30/12/2099")
    private LocalDate dataAtendimento;

    @Schema(description = "Paciente que recebe o atendimento")
    private AtendimentoPacienteDTO paciente;

    @Schema(description = "Médico que realiza o atendimento")
    private AtendimentoMedicoDTO medico;

    @Schema(description = "Laudo do atendimento", example = "Dor de cabeça")
    private String laudo;

    @Schema(description = "Tipo do atendimento", example = "CONSULTA")
    private String tipoDeAtendimento;

    @Schema(description = "Receita médica", example = "Dipirona")
    private String receita;

    @Schema(description = "Valor do atendimento", example = "200")
    private Double valorDoAtendimento;

}
