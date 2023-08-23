package br.com.dbc.wbhealth.model.dto.medico;

import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MedicoNovoOutputDTO extends MedicoOutputDTO{
    @Schema(description = "Usuário criado automaticamente para o médico")
    private UsuarioOutputDTO usuario;
}
