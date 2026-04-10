package atividade_biblioteca.service;

import javax.transaction.Transactional;

import atividade_biblioteca.dao.EmprestimoDAO;
import atividade_biblioteca.dao.LivroDAO;
import atividade_biblioteca.domain.Emprestimo;
import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.service.generic.GenericService;

public class EmprestimoService 
    extends GenericService<Emprestimo, Long> 
    implements IEmprestimoService {

    private EmprestimoDAO emprestimoDAO;
    private LivroDAO livroDAO;

    public EmprestimoService(EmprestimoDAO dao, LivroDAO livroDAO) {
        super(dao);
        this.emprestimoDAO = dao;
        this.livroDAO = livroDAO;
    }

@Override
@Transactional
public void adicionarLivro(Long idEmprestimo, Long idLivro) throw new RuntimeException("Livro já está emprestado"); {

    Emprestimo emp = emprestimoDAO.consultar(idEmprestimo);

    Livro livro = livroDAO.consultar(idLivro);

    // 🔥 validação correta
    if (emprestimoDAO.existeLivroEmprestado(idLivro)) {
        throw new RuntimeException("Livro já está emprestado");
    }

    emp.adicionarLivro(livro);

    emprestimoDAO.alterar(emp);
    }
}

    @Override
    @Transactional
    public void removerLivro(Long idEmprestimo, Long idLivro) {

        Emprestimo emp = emprestimoDAO.consultar(idEmprestimo);
        Livro livro = livroDAO.consultar(idLivro);

        emp.removerLivro(livro);

        emprestimoDAO.alterar(emp);
    }

    @Override
    @Transactional
    public void finalizarEmprestimo(Long idEmprestimo) {

        Emprestimo emp = emprestimoDAO.consultar(idEmprestimo);

        emp.finalizarEmprestimo();

        emprestimoDAO.alterar(emp);
    }

    @Override
    @Transactional
    public void cancelarEmprestimo(Long idEmprestimo) {

        Emprestimo emp = emprestimoDAO.consultar(idEmprestimo);

        emp.cancelarEmprestimo();

        emprestimoDAO.alterar(emp);
    }
}