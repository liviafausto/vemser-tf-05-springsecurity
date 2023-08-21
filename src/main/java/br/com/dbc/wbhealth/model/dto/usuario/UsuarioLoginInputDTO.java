package br.com.dbc.wbhealth.model.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UsuarioLoginInputDTO {
    @NotNull
    @Schema(description = "Nome de usuário", required = true)
    private String login;

    @NotNull
    @Schema(description = "Senha do usuário", required = true)
    private String senha;
}
