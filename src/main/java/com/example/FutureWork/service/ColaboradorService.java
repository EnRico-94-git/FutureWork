package com.example.FutureWork.service;

import com.example.FutureWork.model.Colaborador;
import com.example.FutureWork.model.ModeloTrabalho;
import com.example.FutureWork.model.NivelIA;
import com.example.FutureWork.dto.ColaboradorDTO;
import com.example.FutureWork.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository repository;

    public Page<Colaborador> listarColaboradores(Pageable pageable) {
        return repository.findByAtivoTrue(pageable);
    }

    public Optional<Colaborador> buscarPorId(Long id) {
        return repository.findById(id).filter(Colaborador::isAtivo);
    }

    public List<Colaborador> buscarPorModeloTrabalho(ModeloTrabalho modelo) {
        return repository.findByModeloTrabalho(modelo);
    }

    public List<Colaborador> buscarPorHabilidade(String habilidade) {
        return repository.findByHabilidade(habilidade);
    }

    public Colaborador criarColaborador(ColaboradorDTO dto) {
        // Verificar se email já existe
        if (!repository.findByEmail(dto.getEmail()).isEmpty()) {
            throw new RuntimeException("Email já cadastrado");
        }

        Colaborador colab = new Colaborador();
        colab.setNome(dto.getNome());
        colab.setEmail(dto.getEmail());
        colab.setHabilidades(dto.getHabilidades());
        colab.setModeloTrabalho(ModeloTrabalho.valueOf(dto.getModeloTrabalho()));
        colab.setNivelIA(NivelIA.valueOf(dto.getNivelIA()));
        return repository.save(colab);
    }

    public Optional<Colaborador> atualizarColaborador(Long id, ColaboradorDTO dto) {
        return repository.findById(id)
                .filter(Colaborador::isAtivo)
                .map(colab -> {
                    // Verificar se email já existe para outro colaborador
                    List<Colaborador> existentes = repository.findByEmail(dto.getEmail());
                    if (!existentes.isEmpty() && !existentes.get(0).getId().equals(id)) {
                        throw new RuntimeException("Email já cadastrado para outro colaborador");
                    }

                    colab.setNome(dto.getNome());
                    colab.setEmail(dto.getEmail());
                    colab.setHabilidades(dto.getHabilidades());
                    colab.setModeloTrabalho(ModeloTrabalho.valueOf(dto.getModeloTrabalho()));
                    colab.setNivelIA(NivelIA.valueOf(dto.getNivelIA()));
                    return repository.save(colab);
                });
    }

    public boolean desativarColaborador(Long id) {
        return repository.findById(id)
                .map(colab -> {
                    colab.setAtivo(false);
                    repository.save(colab);
                    return true;
                })
                .orElse(false);
    }

    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new HashMap<>();

        List<Colaborador> todos = repository.findAll();
        List<Colaborador> ativos = todos.stream()
                .filter(Colaborador::isAtivo)
                .toList();

        stats.put("totalColaboradores", ativos.size());

        // Estatísticas por modelo de trabalho
        Map<String, Long> porModelo = new HashMap<>();
        for (ModeloTrabalho modelo : ModeloTrabalho.values()) {
            long count = ativos.stream()
                    .filter(c -> c.getModeloTrabalho() == modelo)
                    .count();
            porModelo.put(modelo.name(), count);
        }
        stats.put("porModeloTrabalho", porModelo);

        // Estatísticas por nível de IA
        Map<String, Long> porNivel = new HashMap<>();
        for (NivelIA nivel : NivelIA.values()) {
            long count = ativos.stream()
                    .filter(c -> c.getNivelIA() == nivel)
                    .count();
            porNivel.put(nivel.name(), count);
        }
        stats.put("porNivelIA", porNivel);

        return stats;
    }
}