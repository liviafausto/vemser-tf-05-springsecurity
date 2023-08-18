package br.com.dbc.wbhealth.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PACIENTE")
public class PacienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PACIENTE_SEQ")
    @SequenceGenerator(name = "PACIENTE_SEQ", sequenceName = "seq_paciente", allocationSize = 1)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_hospital", referencedColumnName = "id_hospital")
    private HospitalEntity hospitalEntity;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa")
    private PessoaEntity pessoa;

    @JsonIgnore
    @OneToMany(
            mappedBy = "pacienteEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<AtendimentoEntity> atendimentos;

}
