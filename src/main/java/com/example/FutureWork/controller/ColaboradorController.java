package com.example.FutureWork.controller;

import com.example.FutureWork.dto.ColaboradorDTO;
import com.example.FutureWork.model.Colaborador;
import com.example.FutureWork.model.ModeloTrabalho;
import com.example.FutureWork.model.NivelIA;
import com.example.FutureWork.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorService service;

    @GetMapping
    public String listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Colaborador> colaboradoresPage = service.listarColaboradores(pageable);

        model.addAttribute("colaboradoresPage", colaboradoresPage);

        if (colaboradoresPage.getTotalPages() > 0) {
            int totalPages = colaboradoresPage.getTotalPages();
            int currentPage = colaboradoresPage.getNumber();
            int start = Math.max(0, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages - 1);

            model.addAttribute("pageNumbers",
                    IntStream.rangeClosed(start, end)
                            .boxed()
                            .collect(Collectors.toList()));
        }

        return "colaboradores/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        System.out.println("🔍 DEBUG: Carregando formulário de NOVO colaborador");

        ColaboradorDTO dto = new ColaboradorDTO();
        model.addAttribute("colaborador", dto);
        model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
        model.addAttribute("niveisIA", NivelIA.values());

        System.out.println("✅ Modelos de Trabalho: " + ModeloTrabalho.values().length);
        System.out.println("✅ Níveis de IA: " + NivelIA.values().length);

        return "colaboradores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("🔍 DEBUG: Carregando formulário para EDITAR colaborador ID: " + id);

        return service.buscarPorId(id)
                .map(colaborador -> {
                    ColaboradorDTO dto = converterParaDTO(colaborador);
                    model.addAttribute("colaborador", dto);
                    model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
                    model.addAttribute("niveisIA", NivelIA.values());

                    System.out.println("✅ Colaborador encontrado: " + colaborador.getNome());

                    return "colaboradores/formulario";
                })
                .orElseGet(() -> {
                    System.err.println("❌ Colaborador não encontrado: " + id);
                    redirectAttributes.addFlashAttribute("erro", "Colaborador não encontrado");
                    return "redirect:/colaboradores";
                });
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return service.buscarPorId(id)
                .map(colaborador -> {
                    model.addAttribute("colaborador", colaborador);
                    return "colaboradores/visualizar";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Colaborador não encontrado");
                    return "redirect:/colaboradores";
                });
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("colaborador") ColaboradorDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // 🔍 DEBUG: Log completo do que foi recebido
        System.out.println("\n========================================");
        System.out.println("🔍 DEBUG: POST /colaboradores/salvar");
        System.out.println("========================================");
        System.out.println("ID: " + dto.getId());
        System.out.println("Nome: " + dto.getNome());
        System.out.println("Email: " + dto.getEmail());
        System.out.println("Habilidades: " + dto.getHabilidades());
        System.out.println("Modelo Trabalho (String): " + dto.getModeloTrabalho());
        System.out.println("Nível IA (String): " + dto.getNivelIA());
        System.out.println("Tem erros de validação: " + result.hasErrors());

        // 🔍 DEBUG: Mostrar erros de validação
        if (result.hasErrors()) {
            System.err.println("\n❌ ERROS DE VALIDAÇÃO ENCONTRADOS:");
            result.getAllErrors().forEach(error -> {
                System.err.println("  - " + error.getDefaultMessage());
            });
            System.out.println("========================================\n");

            // Recarregar enums no model
            model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
            model.addAttribute("niveisIA", NivelIA.values());
            model.addAttribute("erro", "Por favor, corrija os erros no formulário");

            return "colaboradores/formulario";
        }

        // 🔍 DEBUG: Validar valores dos enums
        System.out.println("\n🔍 Validando valores dos Enums...");
        try {
            ModeloTrabalho.valueOf(dto.getModeloTrabalho());
            System.out.println("✅ ModeloTrabalho válido: " + dto.getModeloTrabalho());
        } catch (Exception e) {
            System.err.println("❌ ModeloTrabalho INVÁLIDO: " + dto.getModeloTrabalho());
            System.err.println("   Valores aceitos: " + java.util.Arrays.toString(ModeloTrabalho.values()));

            model.addAttribute("erro", "Modelo de trabalho inválido: " + dto.getModeloTrabalho());
            model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
            model.addAttribute("niveisIA", NivelIA.values());
            return "colaboradores/formulario";
        }

        try {
            NivelIA.valueOf(dto.getNivelIA());
            System.out.println("✅ NivelIA válido: " + dto.getNivelIA());
        } catch (Exception e) {
            System.err.println("❌ NivelIA INVÁLIDO: " + dto.getNivelIA());
            System.err.println("   Valores aceitos: " + java.util.Arrays.toString(NivelIA.values()));

            model.addAttribute("erro", "Nível de IA inválido: " + dto.getNivelIA());
            model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
            model.addAttribute("niveisIA", NivelIA.values());
            return "colaboradores/formulario";
        }

        // 🔍 DEBUG: Tentar salvar
        try {
            if (dto.getId() == null) {
                System.out.println("\n💾 Tentando CRIAR novo colaborador...");
                Colaborador criado = service.criarColaborador(dto);
                System.out.println("✅ SUCESSO! Colaborador criado com ID: " + criado.getId());
                System.out.println("========================================\n");

                redirectAttributes.addFlashAttribute("sucesso", "Colaborador criado com sucesso!");
            } else {
                System.out.println("\n💾 Tentando ATUALIZAR colaborador ID: " + dto.getId());
                Colaborador atualizado = service.atualizarColaborador(dto.getId(), dto)
                        .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
                System.out.println("✅ SUCESSO! Colaborador atualizado: " + atualizado.getNome());
                System.out.println("========================================\n");

                redirectAttributes.addFlashAttribute("sucesso", "Colaborador atualizado com sucesso!");
            }

            return "redirect:/colaboradores";

        } catch (Exception e) {
            System.err.println("\n❌ ERRO AO SALVAR:");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Tipo: " + e.getClass().getName());
            e.printStackTrace();
            System.out.println("========================================\n");

            model.addAttribute("erro", "Erro ao salvar: " + e.getMessage());
            model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
            model.addAttribute("niveisIA", NivelIA.values());

            return "colaboradores/formulario";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("🔍 DEBUG: Tentando excluir colaborador ID: " + id);

        try {
            if (service.desativarColaborador(id)) {
                System.out.println("✅ Colaborador desativado com sucesso");
                redirectAttributes.addFlashAttribute("sucesso", "Colaborador excluído com sucesso!");
            } else {
                System.err.println("❌ Colaborador não encontrado");
                redirectAttributes.addFlashAttribute("erro", "Colaborador não encontrado");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao excluir: " + e.getMessage());
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir colaborador: " + e.getMessage());
        }

        return "redirect:/colaboradores";
    }

    private ColaboradorDTO converterParaDTO(Colaborador colaborador) {
        ColaboradorDTO dto = new ColaboradorDTO();
        dto.setId(colaborador.getId());
        dto.setNome(colaborador.getNome());
        dto.setEmail(colaborador.getEmail());
        dto.setHabilidades(colaborador.getHabilidades());
        dto.setModeloTrabalho(colaborador.getModeloTrabalho().name());
        dto.setNivelIA(colaborador.getNivelIA().name());
        return dto;
    }
}