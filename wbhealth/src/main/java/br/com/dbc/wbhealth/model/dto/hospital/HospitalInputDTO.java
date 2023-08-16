package br.com.dbc.wbhealth.model.dto.hospital;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalInputDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    @Schema(description = "Nome do Hospital", example = "Hospital Santa Maria", required = true)
    private String nome;
}
