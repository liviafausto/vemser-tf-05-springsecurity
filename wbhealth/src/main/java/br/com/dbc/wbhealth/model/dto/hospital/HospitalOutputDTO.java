package br.com.dbc.wbhealth.model.dto.hospital;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospitalOutputDTO {

    @Schema(description = "Id do Hospital", example = "6")
    private Integer idHospital;

    @Schema(description = "Nome do Hospital", example = "Santa luzia")
    private String nome;

}
