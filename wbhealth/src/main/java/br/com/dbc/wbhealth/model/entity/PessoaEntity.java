package br.com.dbc.wbhealth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PESSOA")
public class PessoaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESSOA_SEQ")
    @SequenceGenerator(name = "PESSOA_SEQ", sequenceName = "seq_pessoa", allocationSize = 1)
    @Column(name = "id_pessoa")
    private Integer idPessoa;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cep")
    private String cep;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "salario_mensal")
    private Double salarioMensal;

    @Column(name = "email")
    private String email;

    public PessoaEntity(String nome, String cep, LocalDate dataNascimento, String cpf, Double salarioMensal, String email) {
        this.nome = nome;
        this.cep = cep;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.salarioMensal = salarioMensal;
        this.email = email;
    }

}
