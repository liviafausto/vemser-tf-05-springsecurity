package br.com.dbc.wbhealth.model.dto.atendimento;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtendimentoInputDTO {

    @Positive
    @NotNull
    @Schema(description = "Id do hospital de atendimento", example = "2", required = true)
    private Integer idHospital;

    @Positive
    @NotNull
    @Schema(description = "Id do paciente", example = "4", required = true)
    private Integer idPaciente;

    @Positive
    @NotNull
    @Schema(description = "Id do médico", example = "1", required = true)
    private Integer idMedico;

    @NotNull
    @FutureOrPresent
    @Schema(description = "Data de atendimento", example = "30/12/2099", required = true)
    private LocalDate dataAtendimento;

    @Schema(description = "Laudo do atendimento", example = "Dor de cabeça")
    private String laudo;

    @NotNull
    @NotBlank
    @Schema(description = "Tipo do atendimento", example = "CONSULTA", required = true)
    private String tipoDeAtendimento;

    @Schema(description = "Receita médica", example = "Dipirona")
    private String receita;

    @Positive
    @Nullable
    @Schema(description = "Valor do atendimento", example = "200", required = true)
    private Double valorDoAtendimento;

}
