package br.com.dbc.wbhealth.model.dto.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoInputDTO {
    @NotBlank
    @Schema(description = "Nome do médico", required = true)
    private String nome;

    @NotBlank
    @Size(min = 8, max = 8)
    @Schema(description = "Número CEP do médico", example = "12345678", required = true)
    private String cep;

    @NotNull
    @PastOrPresent
    @Schema(description = "Data de nascimento do médico", example = "1995-04-22", required = true)
    private LocalDate dataNascimento;

    @CPF
    @Schema(description = "Número de CPF do médico", example = "28283051040", required = true)
    private String cpf;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Salário mensal do médico", example = "9000", required = true)
    private Double salarioMensal;

    @Email
    @NotBlank
    @Schema(description = "Email pessoal do médico", required = true)
    private String email;

    @NotNull
    @Positive
    @Schema(description = "Identificador do hospital onde o medico trabalha", required = true)
    private Integer idHospital;

    @NotBlank
    @Size(min = 13, max = 13)
    @Schema(description = "CRM do medico", example = "AM-7654321/82", required = true)
    private String crm;

}
