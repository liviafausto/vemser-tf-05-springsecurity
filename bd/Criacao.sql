-- //////////////////////////////// HOSPITAL ////////////////////////////////// --
CREATE TABLE Hospital (
    id_hospital NUMBER(3) NOT NULL,
    nome VARCHAR2(50) NOT NULL,
     CONSTRAINT PK_HOSPITAL PRIMARY KEY(id_hospital)     
);

-- ////////////////////////////////// PESSOA ////////////////////////////////// --
CREATE TABLE Pessoa(
    id_pessoa NUMBER (3) NOT NULL,
    nome VARCHAR2(50) NOT NULL,
    cep CHAR(8) NOT NULL,
    data_nascimento DATE,
    cpf CHAR(11) UNIQUE NOT NULL,
    salario_mensal DECIMAL(7,2),
    email VARCHAR2(75),
     CONSTRAINT pk_Pessoa PRIMARY KEY (id_pessoa)
);




CREATE SEQUENCE seq_pessoa
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

-- ////////////////////////////////// PACIENTE ////////////////////////////////// --
CREATE TABLE Paciente(
	id_paciente NUMBER(3) NOT NULL,
	id_hospital NUMBER(3) NOT NULL,
	id_pessoa NUMBER(3) NOT NULL,
	 CONSTRAINT PK_PACIENTE PRIMARY KEY (id_paciente),
	 CONSTRAINT FK_PACIENTE_HOSPITAL FOREIGN KEY (id_hospital) REFERENCES Hospital(id_hospital),
	 CONSTRAINT FK_PACIENTE_PESSOA FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa)
);

---- SEQUENCE PACIENTE ----
CREATE SEQUENCE SEQ_PACIENTE
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
	
-- ////////////////////////////////// FUNCIONÁRIO ////////////////////////////////// --
CREATE TABLE Funcionario (
	id_funcionario NUMBER(10) NOT NULL,
	id_hospital NUMBER(10) NOT NULL,
	id_pessoa NUMBER(10) NOT NULL,
 	 CONSTRAINT PK_FUNCIONARIO PRIMARY KEY (id_funcionario),
 	 CONSTRAINT FK_FUNCIONARIO_HOSPITAL FOREIGN KEY (id_hospital) REFERENCES Hospital(id_hospital),
 	 CONSTRAINT FK_FUNCIONARIO_PESSOA FOREIGN KEY (id_pessoa) REFERENCES Pessoa(id_pessoa)
);

---- SEQUENCE FUNCIONARIO ----
CREATE SEQUENCE SEQ_FUNCIONARIO
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;

-- ////////////////////////////////// MEDICO ////////////////////////////////// --	
CREATE TABLE Medico (
    id_pessoa NUMBER(10) NOT NULL,
    id_medico NUMBER(10) NOT NULL,
    id_hospital NUMBER(10) NOT NULL,
    crm CHAR(13) UNIQUE NOT NULL,
     CONSTRAINT PK_MEDICO PRIMARY KEY (id_medico),
     CONSTRAINT FK_MEDICO_PESSOA FOREIGN KEY (id_pessoa) REFERENCES PESSOA(id_pessoa),
     CONSTRAINT FK_MEDICO_HOSPITAL FOREIGN KEY (id_hospital) REFERENCES HOSPITAL(id_hospital)
);

CREATE SEQUENCE SEQ_MEDICO
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;
 
-- ////////////////////////////////// ATENDIMENTO ////////////////////////////////// --	
CREATE TABLE Atendimento(
    id_atendimento NUMBER(10) NOT NULL,
    id_hospital NUMBER(10) NOT NULL,
    id_paciente NUMBER(10) NOT NULL,
    id_medico NUMBER(10) NOT NULL,
    data_atendimento DATE NOT NULL,
    laudo VARCHAR2(100) NOT NULL,
    tipo_de_atendimento VARCHAR2(10) NOT NULL,
    valor_atendimento NUMBER(7,2) NOT NULL,
     CONSTRAINT PK_ATENDIMENTO PRIMARY KEY(id_atendimento),
     CONSTRAINT FK_ATENDIMENTO_HOSPITAL FOREIGN KEY(id_hospital) REFERENCES HOSPITAL(id_hospital),
     CONSTRAINT FK_ATENDIMENTO_PACIENTE FOREIGN KEY(id_paciente) REFERENCES PACIENTE(id_paciente),
     CONSTRAINT FK_ATENDIMENTO_MEDICO FOREIGN KEY(id_medico) REFERENCES MEDICO(id_medico)
);


CREATE SEQUENCE SEQ_ATENDIMENTO
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE SEQ_HOSPITAL
START WITH 2
INCREMENT BY 1
NOCACHE
NOCYCLE;

SELECT * FROM MEDICO m 

SELECT * FROM MEDICO 
INNER JOIN PESSOA ON MEDICO.ID_MEDICO = 3 AND PESSOA.ID_PESSOA = MEDICO.ID_PESSOA
