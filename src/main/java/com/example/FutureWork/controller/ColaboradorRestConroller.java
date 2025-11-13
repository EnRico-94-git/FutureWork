package com.example.FutureWork.controller;


import com.example.FutureWork.dto.ColaboradorDTO;
import com.example.FutureWork.model.Colaborador;
import com.example.FutureWork.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/colaboradores")
@CrossOrigin(origins = "*") // Permite requests de qualquer origem (útil para frontends separados)
public class ColaboradorRestController {

    @Autowired
    private ColaboradorService colaboradorService;

    /**
     * GET - Lista todos os colaboradores com paginação
     * URL: GET /api/colaboradores?page=0&size=10&sort=nome
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarColaboradores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<Colaborador> colaboradoresPage = colaboradorService.listarColaboradores(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("colaboradores", colaboradoresPage.getContent());
            response.put("paginaAtual", colaboradoresPage.getNumber());
            response.put("totalItens", colaboradoresPage.getTotalElements());
            response.put("totalPaginas", colaboradoresPage.getTotalPages());
            response.put("tamanhoPagina", size);
            response.put("ordenacao", sort);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao listar colaboradores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET - Busca colaborador por ID
     * URL: GET /api/colaboradores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarColaborador(@PathVariable Long id) {
        try {
            Optional<Colaborador> colaborador = colaboradorService.buscarPorId(id);

            if (colaborador.isPresent()) {
                return ResponseEntity.ok(colaborador.get());
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Colaborador não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao buscar colaborador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * POST - Cria novo colaborador
     * URL: POST /api/colaboradores
     */
    @PostMapping
    public ResponseEntity<?> criarColaborador(@Valid @RequestBody ColaboradorDTO colaboradorDTO) {
        try {
            Colaborador novoColaborador = colaboradorService.criarColaborador(colaboradorDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("mensagem", "Colaborador criado com sucesso!");
            response.put("colaborador", novoColaborador);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao criar colaborador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * PUT - Atualiza colaborador existente
     * URL: PUT /api/colaboradores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarColaborador(
            @PathVariable Long id,
            @Valid @RequestBody ColaboradorDTO colaboradorDTO) {

        try {
            Optional<Colaborador> colaboradorAtualizado = colaboradorService.atualizarColaborador(id, colaboradorDTO);

            if (colaboradorAtualizado.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensagem", "Colaborador atualizado com sucesso!");
                response.put("colaborador", colaboradorAtualizado.get());

                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Colaborador não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao atualizar colaborador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * DELETE - Remove colaborador (desativa)
     * URL: DELETE /api/colaboradores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerColaborador(@PathVariable Long id) {
        try {
            boolean removido = colaboradorService.desativarColaborador(id);

            if (removido) {
                Map<String, String> response = new HashMap<>();
                response.put("mensagem", "Colaborador removido com sucesso!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Colaborador não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao remover colaborador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET - Busca colaboradores por modelo de trabalho
     * URL: GET /api/colaboradores/modelo/{modelo}
     */
    @GetMapping("/modelo/{modelo}")
    public ResponseEntity<?> buscarPorModeloTrabalho(@PathVariable String modelo) {
        try {
            // Aqui você precisaria adicionar um método no service/repository
            // Por enquanto, vamos filtrar manualmente na página
            Pageable pageable = PageRequest.of(0, 100, Sort.by("nome"));
            Page<Colaborador> todos = colaboradorService.listarColaboradores(pageable);

            var filtrados = todos.getContent().stream()
                    .filter(c -> c.getModeloTrabalho().name().equalsIgnoreCase(modelo))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("colaboradores", filtrados);
            response.put("total", filtrados.size());
            response.put("modelo", modelo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao buscar por modelo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET - Health check da API
     * URL: GET /api/colaboradores/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("mensagem", "API Colaboradores está funcionando!");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}