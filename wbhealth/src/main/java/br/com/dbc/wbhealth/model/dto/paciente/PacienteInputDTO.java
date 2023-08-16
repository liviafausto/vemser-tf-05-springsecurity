package br.com.dbc.wbhealth.model.dto.paciente;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class PacienteInputDTO {
    @NotBlank
    @Schema(description = "Nome do paciente", required = true)
    private String nome;

    @NotBlank
    @Size(min = 8, max = 8)
    @Schema(description = "Número CEP do paciente", example = "12345678", required = true)
    private String cep;

    @NotNull
    @PastOrPresent
    @Schema(description = "Data de nascimento do paciente",  example = "1995-04-22", required = true)
    private LocalDate dataNascimento;

    @CPF
    @Schema(description = "Número de CPF do paciente",  example = "28283051040", required = true)
    private String cpf;

    @Email
    @NotBlank
    @Schema(description = "Email pessoal do paciente", required = true)
    private String email;

    @NotNull
    @Positive
    @Schema(description = "Identificador do hospital que fornece o atendimento", required = true)
    private Integer idHospital;

}
