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

    // ================= LISTA =================
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

        model.addAttribute("sort", sort);
        model.addAttribute("size", size);

        return "colaboradores/lista";
    }

    // ================= NOVO =================
    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        System.out.println("🔍 DEBUG: GET /colaboradores/novo");

        // garante sempre um DTO no model
        if (!model.containsAttribute("colaborador")) {
            model.addAttribute("colaborador", new ColaboradorDTO());
        }

        adicionarEnums(model);
        return "colaboradores/formulario";
    }

    // ================= EDITAR =================
    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("🔍 DEBUG: GET /colaboradores/editar/" + id);

        return service.buscarPorId(id)
                .map(colaborador -> {
                    ColaboradorDTO dto = converterParaDTO(colaborador);

                    model.addAttribute("colaborador", dto);
                    adicionarEnums(model);

                    System.out.println("✅ Colaborador encontrado: " + colaborador.getNome());
                    return "colaboradores/formulario";
                })
                .orElseGet(() -> {
                    System.err.println("❌ Colaborador não encontrado: " + id);
                    redirectAttributes.addFlashAttribute("erro", "Colaborador não encontrado");
                    return "redirect:/colaboradores";
                });
    }

    // ================= VISUALIZAR =================
    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        System.out.println("🔍 DEBUG: GET /colaboradores/visualizar/" + id);

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

    // ================= SALVAR =================
    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("colaborador") ColaboradorDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("\n========================================");
        System.out.println("🔍 DEBUG: POST /colaboradores/salvar");
        System.out.println("ID: " + dto.getId());
        System.out.println("Nome: " + dto.getNome());
        System.out.println("Email: " + dto.getEmail());
        System.out.println("Habilidades: " + dto.getHabilidades());
        System.out.println("Modelo Trabalho (string): " + dto.getModeloTrabalho());
        System.out.println("Nível IA (string): " + dto.getNivelIA());
        System.out.println("Tem erros de validação: " + result.hasErrors());
        System.out.println("========================================\n");

        // erros de bean validation
        if (result.hasErrors()) {
            System.err.println("❌ Erros de validação no formulário");
            adicionarEnums(model);
            model.addAttribute("erro", "Por favor, corrija os erros no formulário.");
            return "colaboradores/formulario";
        }

        // validação defensiva dos enums
        try {
            if (dto.getModeloTrabalho() == null || dto.getModeloTrabalho().isBlank()) {
                throw new IllegalArgumentException("Modelo de trabalho é obrigatório");
            }
            ModeloTrabalho.valueOf(dto.getModeloTrabalho());

            if (dto.getNivelIA() == null || dto.getNivelIA().isBlank()) {
                throw new IllegalArgumentException("Nível de IA é obrigatório");
            }
            NivelIA.valueOf(dto.getNivelIA());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Enum inválido: " + e.getMessage());
            adicionarEnums(model);
            model.addAttribute("erro", e.getMessage());
            return "colaboradores/formulario";
        }

        try {
            if (dto.getId() == null) {
                System.out.println("💾 Criando novo colaborador...");
                Colaborador criado = service.criarColaborador(dto);
                System.out.println("✅ Criado com ID: " + criado.getId());
                redirectAttributes.addFlashAttribute("sucesso", "Colaborador criado com sucesso!");
            } else {
                System.out.println("💾 Atualizando colaborador ID: " + dto.getId());
                Colaborador atualizado = service.atualizarColaborador(dto.getId(), dto)
                        .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
                System.out.println("✅ Atualizado: " + atualizado.getNome());
                redirectAttributes.addFlashAttribute("sucesso", "Colaborador atualizado com sucesso!");
            }

            return "redirect:/colaboradores";

        } catch (Exception e) {
            System.err.println("\n❌ ERRO AO SALVAR:");
            e.printStackTrace();

            adicionarEnums(model);
            model.addAttribute("erro", "Erro ao salvar: " + e.getMessage());
            return "colaboradores/formulario";
        }
    }

    // ================= EXCLUIR =================
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

    // ================= HELPERS =================
    private void adicionarEnums(Model model) {
        model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
        model.addAttribute("niveisIA", NivelIA.values());
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
