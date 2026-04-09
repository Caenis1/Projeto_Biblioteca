package atividade_biblioteca.service;

import atividade_biblioteca.domain.Emprestimo;
import atividade_biblioteca.service.generic.IGenericService;

public interface IEmprestimoService extends IGenericService<Emprestimo, Long> {

    void adicionarLivro(Long idEmprestimo, Long idLivro);

    void removerLivro(Long idEmprestimo, Long idLivro);

    void finalizarEmprestimo(Long idEmprestimo);

    void cancelarEmprestimo(Long idEmprestimo);
}
