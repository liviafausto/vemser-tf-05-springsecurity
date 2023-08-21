package br.com.dbc.wbhealth.model.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioOutputDTO {
    @Schema(description = "Identificador do usuário")
    private String idUsuario;

    @Schema(description = "Nome de usuário")
    private String login;

    @Schema(description = "Cargos do usuário")
    private Set<Integer> cargos;
}
