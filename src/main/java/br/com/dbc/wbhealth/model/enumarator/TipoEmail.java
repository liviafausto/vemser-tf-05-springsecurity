package br.com.dbc.wbhealth.model.enumarator;

public enum TipoEmail {

    CONFIRMACAO(0, "Confirmação de atendimento!"), ATUALIZACAO(1, "Alteração no atendimento realizada!"), CANCELAMENTO(2, "Atendimento cancelado!");

    private int codigo;

    private String titulo;

    TipoEmail(int codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
    }

    public static TipoEmail valueOf(int code) {
        for (TipoEmail valor : TipoEmail.values()) {
            if (valor.getCodigo() == code) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Código inválido");
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

}
