package br.com.dbc.wbhealth.model.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class UsuarioSenhaInputDTO {
    @NotNull
    @Schema(description = "Senha do usuário", required = true, example = "12345")
    private String senha;
}
