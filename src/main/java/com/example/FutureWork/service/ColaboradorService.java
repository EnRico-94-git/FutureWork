package com.example.FutureWork.service;

import com.futurework.model.Colaborador;
import com.futurework.model.ModeloTrabalho;
import com.futurework.model.NivelIA;
import com.futurework.dto.ColaboradorDTO;
import com.futurework.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
}