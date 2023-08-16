package br.com.dbc.wbhealth.model.dto.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoOutputDTO {
    @Schema(description = "Identificador de médico; associado à pessoa")
    private Integer idMedico;

    @Schema(description = "Identificador do hospital onde o médico trabalha")
    private Integer idHospital;

    @Schema(description = "Identificador da pessoa")
    private Integer idPessoa;

    @Schema(description = "Nome do médico")
    private String nome;

    @Schema(description = "Número CEP do médico", example = "12345678")
    private String cep;

    @Schema(description = "Data de nascimento do médico", example = "1995-04-22")
    private LocalDate dataNascimento;

    @Schema(description = "Número de CPF do médico", example = "28283051040")
    private String cpf;

    @Schema(description = "Salário mensal do médico", example = "9000")
    private Double salarioMensal;

    @Schema(description = "Email pessoal do médico")
    private String email;

    @Schema(description = "CRM do medico", example = "AM-7654321/82")
    private String crm;
}
