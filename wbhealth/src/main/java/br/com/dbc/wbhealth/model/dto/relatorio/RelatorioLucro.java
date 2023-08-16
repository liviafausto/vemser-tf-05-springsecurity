package br.com.dbc.wbhealth.model.dto.relatorio;

import br.com.dbc.wbhealth.model.enumarator.TipoDeAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RelatorioLucro {

    @Schema(name = "Tipo de atendimento", description = "Refere ao tipo do atendimento registrado no banco.")
    private TipoDeAtendimento tipoDeAtendimento;

    @Schema(name = "Lucro", description = "Refere ao lucro até o momento buscado.")
    private Double lucro;

    public RelatorioLucro(TipoDeAtendimento tipoDeAtendimento, Double lucro) {
        this.tipoDeAtendimento = tipoDeAtendimento;
        this.lucro = lucro;
    }
}
