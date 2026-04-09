package atividade_biblioteca.service;

import javax.transaction.Transactional;

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
    public void adicionarLivro(Long idEmprestimo, Long idLivro) {

        Emprestimo emp = emprestimoDAO.consultar(idEmprestimo);
        Livro livro = livroDAO.consultar(idLivro);

        if (livro.isEmprestado()) {
            throw new RuntimeException("Livro já emprestado");
        }

        emp.adicionarLivro(livro);
        livro.setEmprestado(true);

        emprestimoDAO.alterar(emp);
    }

    @Override
    @Transactional
    public void removerLivro(Long idEmprestimo, Long idLivro) {

        Emprestimo emp = emprestimoDAO.consultar(idEmprestimo);
        Livro livro = livroDAO.consultar(idLivro);

        emp.removerLivro(livro);
        livro.setEmprestado(false);

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

        emp.getItens().forEach(item -> {
            item.getLivro().setEmprestado(false);
        });

        emp.cancelarEmprestimo();

        emprestimoDAO.alterar(emp);
    }
}