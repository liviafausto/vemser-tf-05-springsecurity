package br.com.dbc.wbhealth.model.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioOutputDTO {
    @Schema(description = "Identificador do usuário")
    private String idUsuario;

    @Schema(description = "Nome de usuário")
    private String login;
}
