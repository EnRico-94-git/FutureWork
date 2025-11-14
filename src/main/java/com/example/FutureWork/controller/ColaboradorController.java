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

        // Números de página para a paginação
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
        model.addAttribute("colaborador", new ColaboradorDTO());
        model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
        model.addAttribute("niveisIA", NivelIA.values());
        return "colaboradores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return service.buscarPorId(id)
                .map(colaborador -> {
                    ColaboradorDTO dto = converterParaDTO(colaborador);
                    model.addAttribute("colaborador", dto);
                    model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
                    model.addAttribute("niveisIA", NivelIA.values());
                    return "colaboradores/formulario";
                })
                .orElseGet(() -> {
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

        if (result.hasErrors()) {
            model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
            model.addAttribute("niveisIA", NivelIA.values());
            return "colaboradores/formulario";
        }

        try {
            if (dto.getId() == null) {
                service.criarColaborador(dto);
                redirectAttributes.addFlashAttribute("sucesso", "Colaborador criado com sucesso!");
            } else {
                service.atualizarColaborador(dto.getId(), dto)
                        .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
                redirectAttributes.addFlashAttribute("sucesso", "Colaborador atualizado com sucesso!");
            }
            return "redirect:/colaboradores";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("modelosTrabalho", ModeloTrabalho.values());
            model.addAttribute("niveisIA", NivelIA.values());
            return "colaboradores/formulario";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (service.desativarColaborador(id)) {
                redirectAttributes.addFlashAttribute("sucesso", "Colaborador excluído com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Colaborador não encontrado");
            }
        } catch (Exception e) {
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