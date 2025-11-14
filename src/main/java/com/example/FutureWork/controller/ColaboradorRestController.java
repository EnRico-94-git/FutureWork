package com.example.FutureWork.controller;

import com.example.FutureWork.dto.ColaboradorDTO;
import com.example.FutureWork.model.Colaborador;
import com.example.FutureWork.model.ModeloTrabalho;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colaboradores")
@CrossOrigin(origins = "*")
public class ColaboradorRestController {

    @Autowired
    private ColaboradorService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
            Page<Colaborador> colaboradoresPage = service.listarColaboradores(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("colaboradores", colaboradoresPage.getContent());
            response.put("currentPage", colaboradoresPage.getNumber());
            response.put("totalItems", colaboradoresPage.getTotalElements());
            response.put("totalPages", colaboradoresPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/modelo/{modelo}")
    public ResponseEntity<List<Colaborador>> buscarPorModelo(@PathVariable String modelo) {
        try {
            ModeloTrabalho modeloTrabalho = ModeloTrabalho.valueOf(modelo.toUpperCase());
            List<Colaborador> colaboradores = service.buscarPorModeloTrabalho(modeloTrabalho);
            return ResponseEntity.ok(colaboradores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/habilidade/{habilidade}")
    public ResponseEntity<List<Colaborador>> buscarPorHabilidade(@PathVariable String habilidade) {
        List<Colaborador> colaboradores = service.buscarPorHabilidade(habilidade);
        return ResponseEntity.ok(colaboradores);
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ColaboradorDTO dto) {
        try {
            Colaborador colaborador = service.criarColaborador(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(colaborador);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ColaboradorDTO dto) {
        try {
            return service.atualizarColaborador(id, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (service.desativarColaborador(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        Map<String, Object> stats = service.obterEstatisticas();
        return ResponseEntity.ok(stats);
    }
}