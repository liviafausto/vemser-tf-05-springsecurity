package br.com.dbc.wbhealth.model.entity;

import br.com.dbc.wbhealth.model.enumarator.TipoDeAtendimento;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity(name = "ATENDIMENTO")
public class AtendimentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATENDIMENTO_SEQ")
    @SequenceGenerator(name = "ATENDIMENTO_SEQ", sequenceName = "SEQ_ATENDIMENTO", allocationSize = 1)
    @Column(name = "id_atendimento")
    private Integer idAtendimento;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_hospital", referencedColumnName = "id_hospital")
    private HospitalEntity hospitalEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    private PacienteEntity pacienteEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_medico", referencedColumnName = "id_medico")
    private MedicoEntity medicoEntity;

    @Column(name = "data_atendimento")
    private LocalDate dataAtendimento;

    @Column(name = "laudo")
    private String laudo;

    @Column(name = "tipo_de_atendimento")
    @Enumerated(EnumType.STRING) // Tornar cardinal depois
    private TipoDeAtendimento tipoDeAtendimento;

    @Column(name = "receita")
    private String receita;

    @Column(name = "valor_atendimento")
    private Double valorDoAtendimento;

}
