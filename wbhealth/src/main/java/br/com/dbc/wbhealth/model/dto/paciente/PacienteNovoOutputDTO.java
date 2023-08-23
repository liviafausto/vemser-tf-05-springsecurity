package br.com.dbc.wbhealth.model.dto.paciente;

import br.com.dbc.wbhealth.model.dto.usuario.UsuarioOutputDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacienteNovoOutputDTO extends PacienteOutputDTO{
    @Schema(description = "Usuário criado automaticamente para o paciente")
    private UsuarioOutputDTO usuario;
}
