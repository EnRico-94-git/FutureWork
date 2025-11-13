package com.example.FutureWork.repository;


import com.example.FutureWork.model.Colaborador;
import com.example.FutureWork.model.ModeloTrabalho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    Page<Colaborador> findAll(Pageable pageable);

    List<Colaborador> findByModeloTrabalho(ModeloTrabalho modeloTrabalho);

    @Query("SELECT c FROM Colaborador c WHERE c.habilidades LIKE %:habilidade% AND c.ativo = true")
    List<Colaborador> findByHabilidade(@Param("habilidade") String habilidade);

    Page<Colaborador> findByAtivoTrue(Pageable pageable);

    @Query("SELECT c FROM Colaborador c WHERE c.email = :email AND c.ativo = true")
    List<Colaborador> findByEmail(@Param("email") String email);
}