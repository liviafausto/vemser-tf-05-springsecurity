package br.com.dbc.wbhealth.model.enumarator;

public enum TipoDeAtendimento {
    CONSULTA(1), CIRURGIA(2), EXAME(3), RETORNO(4);

    private int codigo;

    TipoDeAtendimento(int codigo) {
        this.codigo = codigo;
    }

    public static TipoDeAtendimento valueOf(int code) {
        for (TipoDeAtendimento valor : TipoDeAtendimento.values()) {
            if (valor.getCodigo() == code) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Código inválido");
    }

    public static TipoDeAtendimento getTipo(String tipo) {
        for (TipoDeAtendimento valor : TipoDeAtendimento.values()) {
            if (valor.name().equals(tipo)) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Código inválido");
    }

    public int getCodigo() {
        return codigo;
    }


}
