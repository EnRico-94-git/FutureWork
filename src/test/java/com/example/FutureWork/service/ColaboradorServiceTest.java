package com.example.FutureWork.service;

import com.example.FutureWork.dto.ColaboradorDTO;
import com.example.FutureWork.model.Colaborador;
import com.example.FutureWork.model.ModeloTrabalho;
import com.example.FutureWork.model.NivelIA;
import com.example.FutureWork.repository.ColaboradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ColaboradorService
 * Arquivo: src/test/java/com/example/FutureWork/service/ColaboradorServiceTest.java
 */
class ColaboradorServiceTest {

    @Mock
    private ColaboradorRepository repository;

    @InjectMocks
    private ColaboradorService service;

    private Colaborador colaboradorTeste;
    private ColaboradorDTO dtoTeste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criar colaborador de teste
        colaboradorTeste = new Colaborador();
        colaboradorTeste.setId(1L);
        colaboradorTeste.setNome("João Silva");
        colaboradorTeste.setEmail("joao@test.com");
        colaboradorTeste.setHabilidades("Java, Spring Boot");
        colaboradorTeste.setModeloTrabalho(ModeloTrabalho.REMOTO);
        colaboradorTeste.setNivelIA(NivelIA.ESPECIALISTA);
        colaboradorTeste.setAtivo(true);

        // Criar DTO de teste
        dtoTeste = new ColaboradorDTO();
        dtoTeste.setNome("Maria Santos");
        dtoTeste.setEmail("maria@test.com");
        dtoTeste.setHabilidades("Python, Machine Learning");
        dtoTeste.setModeloTrabalho("HIBRIDO");
        dtoTeste.setNivelIA("PARCEIRO");
    }

    @Test
    @DisplayName("Deve listar colaboradores com paginação")
    void deveListarColaboradoresComPaginacao() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Colaborador> lista = Arrays.asList(colaboradorTeste);
        Page<Colaborador> page = new PageImpl<>(lista);

        when(repository.findByAtivoTrue(pageable)).thenReturn(page);

        // Act
        Page<Colaborador> resultado = service.listarColaboradores(pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(repository, times(1)).findByAtivoTrue(pageable);
    }

    @Test
    @DisplayName("Deve buscar colaborador por ID")
    void deveBuscarColaboradorPorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(colaboradorTeste));

        // Act
        Optional<Colaborador> resultado = service.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve criar novo colaborador com sucesso")
    void deveCriarNovoColaborador() {
        // Arrange
        when(repository.findByEmail(dtoTeste.getEmail())).thenReturn(new ArrayList<>());
        when(repository.save(any(Colaborador.class))).thenReturn(colaboradorTeste);

        // Act
        Colaborador resultado = service.criarColaborador(dtoTeste);

        // Assert
        assertNotNull(resultado);
        verify(repository, times(1)).findByEmail(dtoTeste.getEmail());
        verify(repository, times(1)).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("Não deve criar colaborador com email duplicado")
    void naoDeveCriarColaboradorComEmailDuplicado() {
        // Arrange
        when(repository.findByEmail(dtoTeste.getEmail()))
                .thenReturn(Arrays.asList(colaboradorTeste));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.criarColaborador(dtoTeste);
        });

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(repository, never()).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("Deve atualizar colaborador existente")
    void deveAtualizarColaboradorExistente() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(colaboradorTeste));
        when(repository.findByEmail(dtoTeste.getEmail())).thenReturn(new ArrayList<>());
        when(repository.save(any(Colaborador.class))).thenReturn(colaboradorTeste);

        // Act
        Optional<Colaborador> resultado = service.atualizarColaborador(1L, dtoTeste);

        // Assert
        assertTrue(resultado.isPresent());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("Deve desativar colaborador (soft delete)")
    void deveDesativarColaborador() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(colaboradorTeste));
        when(repository.save(any(Colaborador.class))).thenReturn(colaboradorTeste);

        // Act
        boolean resultado = service.desativarColaborador(1L);

        // Assert
        assertTrue(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("Não deve desativar colaborador inexistente")
    void naoDeveDesativarColaboradorInexistente() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act
        boolean resultado = service.desativarColaborador(999L);

        // Assert
        assertFalse(resultado);
        verify(repository, times(1)).findById(999L);
        verify(repository, never()).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("Não deve retornar colaborador inativo")
    void naoDeveRetornarColaboradorInativo() {
        // Arrange
        colaboradorTeste.setAtivo(false);
        when(repository.findById(1L)).thenReturn(Optional.of(colaboradorTeste));

        // Act
        Optional<Colaborador> resultado = service.buscarPorId(1L);

        // Assert
        assertFalse(resultado.isPresent());
    }
}