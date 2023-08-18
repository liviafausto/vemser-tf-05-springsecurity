package br.com.dbc.wbhealth.model.dto.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodoDTO {

    @Schema(description = "Data de inicio para a busca", example = "2023-12-15")
    private LocalDate dataInicio;
    @Schema(description = "Data de fim para a busca", example = "2023-12-15")
    private LocalDate dataFim;
}
