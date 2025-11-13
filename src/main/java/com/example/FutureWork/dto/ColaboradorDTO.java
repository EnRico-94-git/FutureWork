package com.example.FutureWork.dto;

import javax.validation.constraints.*;

public class ColaboradorDTO {
    private Long id;

    @NotBlank(message = "{colaborador.nome.notblank}")
    @Size(min = 2, max = 100)
    private String nome;

    @NotBlank(message = "{colaborador.email.notblank}")
    @Email
    private String email;

    @NotBlank(message = "{colaborador.habilidades.notblank}")
    @Size(max = 500)
    private String habilidades;

    @NotNull(message = "{colaborador.modeloTrabalho.notnull}")
    private String modeloTrabalho;

    @NotNull(message = "{colaborador.nivelIA.notnull}")
    private String nivelIA;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }
    public String getModeloTrabalho() { return modeloTrabalho; }
    public void setModeloTrabalho(String modeloTrabalho) { this.modeloTrabalho = modeloTrabalho; }
    public String getNivelIA() { return nivelIA; }
    public void setNivelIA(String nivelIA) { this.nivelIA = nivelIA; }
}