package com.example.FutureWork.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_COLABORADORES")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colab_seq")
    @SequenceGenerator(name = "colab_seq", sequenceName = "COLABORADORES_SEQ", allocationSize = 1)
    @Column(name = "ID_COLABORADOR")
    private Long id;

    @NotBlank(message = "{colaborador.nome.notblank}")
    @Size(min = 2, max = 100, message = "{colaborador.nome.size}")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "{colaborador.email.notblank}")
    @Email(message = "{colaborador.email.invalid}")
    @Column(name = "EMAIL", unique = true, nullable = false, length = 150)
    private String email;

    @NotBlank(message = "{colaborador.habilidades.notblank}")
    @Size(max = 500, message = "{colaborador.habilidades.size}")
    @Column(name = "HABILIDADES", nullable = false, length = 500)
    private String habilidades;

    @Enumerated(EnumType.STRING)
    @Column(name = "MODELO_TRABALHO", nullable = false, length = 20)
    private ModeloTrabalho modeloTrabalho;

    @Enumerated(EnumType.STRING)
    @Column(name = "NIVEL_IA", nullable = false, length = 20)
    private NivelIA nivelIA;

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo = true;

    @Column(name = "DATA_CRIACAO")
    private LocalDateTime dataCriacao;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    // Construtores
    public Colaborador() {}

    public Colaborador(String nome, String email, String habilidades,
                       ModeloTrabalho modeloTrabalho, NivelIA nivelIA) {
        this.nome = nome;
        this.email = email;
        this.habilidades = habilidades;
        this.modeloTrabalho = modeloTrabalho;
        this.nivelIA = nivelIA;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }

    public ModeloTrabalho getModeloTrabalho() { return modeloTrabalho; }
    public void setModeloTrabalho(ModeloTrabalho modeloTrabalho) { this.modeloTrabalho = modeloTrabalho; }

    public NivelIA getNivelIA() { return nivelIA; }
    public void setNivelIA(NivelIA nivelIA) { this.nivelIA = nivelIA; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}