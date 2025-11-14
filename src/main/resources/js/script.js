/**
 * Future Work - Scripts JavaScript
 * Arquivo: src/main/resources/static/js/script.js
 */

// Confirmação de exclusão com nome do colaborador
function confirmarExclusao(nome) {
    return confirm('Tem certeza que deseja excluir o colaborador "' + nome + '"?\n\nEsta ação não pode ser desfeita.');
}

// Auto-hide para alertas após 5 segundos
document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alertas de sucesso/erro
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000); // 5 segundos
    });

    // Adicionar animação suave aos cards
    const cards = document.querySelectorAll('.card');
    cards.forEach(function(card, index) {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        setTimeout(function() {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });

    // Contador de caracteres para textarea de habilidades
    const habilidadesTextarea = document.getElementById('habilidades');
    if (habilidadesTextarea) {
        const maxLength = 500;
        const counter = document.createElement('small');
        counter.className = 'form-text text-muted';
        counter.id = 'charCounter';
        habilidadesTextarea.parentNode.appendChild(counter);

        function updateCounter() {
            const remaining = maxLength - habilidadesTextarea.value.length;
            counter.textContent = remaining + ' caracteres restantes';
            if (remaining < 50) {
                counter.classList.add('text-danger');
                counter.classList.remove('text-muted');
            } else {
                counter.classList.add('text-muted');
                counter.classList.remove('text-danger');
            }
        }

        habilidadesTextarea.addEventListener('input', updateCounter);
        updateCounter(); // Inicializar contador
    }

    // Validação de email em tempo real
    const emailInput = document.getElementById('email');
    if (emailInput) {
        emailInput.addEventListener('blur', function() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (this.value && !emailRegex.test(this.value)) {
                this.classList.add('is-invalid');
                let feedback = this.nextElementSibling;
                if (!feedback || !feedback.classList.contains('invalid-feedback')) {
                    feedback = document.createElement('div');
                    feedback.className = 'invalid-feedback';
                    feedback.textContent = 'Por favor, insira um email válido';
                    this.parentNode.appendChild(feedback);
                }
            } else {
                this.classList.remove('is-invalid');
                const feedback = this.nextElementSibling;
                if (feedback && feedback.classList.contains('invalid-feedback')) {
                    feedback.remove();
                }
            }
        });
    }
});

// Função para copiar email para área de transferência
function copiarEmail(email) {
    navigator.clipboard.writeText(email).then(function() {
        // Criar toast de sucesso
        const toast = document.createElement('div');
        toast.className = 'position-fixed bottom-0 end-0 p-3';
        toast.style.zIndex = '11';
        toast.innerHTML = `
            <div class="toast show" role="alert">
                <div class="toast-header">
                    <strong class="me-auto">✓ Sucesso</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
                </div>
                <div class="toast-body">
                    Email copiado para área de transferência!
                </div>
            </div>
        `;
        document.body.appendChild(toast);

        setTimeout(function() {
            toast.remove();
        }, 3000);
    });
}

// Loading indicator para operações assíncronas
function showLoading() {
    const loading = document.createElement('div');
    loading.id = 'loading-overlay';
    loading.innerHTML = `
        <div class="position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center" 
             style="background: rgba(0,0,0,0.5); z-index: 9999;">
            <div class="spinner-border text-light" role="status">
                <span class="visually-hidden">Carregando...</span>
            </div>
        </div>
    `;
    document.body.appendChild(loading);
}

function hideLoading() {
    const loading = document.getElementById('loading-overlay');
    if (loading) {
        loading.remove();
    }
}

// Prevenir múltiplos submits do formulário
document.querySelectorAll('form').forEach(function(form) {
    form.addEventListener('submit', function() {
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processando...';
        }
    });
});

// Tooltip para todos os elementos com data-bs-toggle="tooltip"
document.addEventListener('DOMContentLoaded', function() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

// Console log para debug
console.log('🚀 Future Work - Scripts carregados com sucesso!');