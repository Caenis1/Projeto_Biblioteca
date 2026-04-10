package atividade_biblioteca.dao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import atividade_biblioteca.domain.Aluno;
import atividade_biblioteca.domain.Emprestimo;
import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.exceptions.DAOException;
import atividade_biblioteca.exceptions.TipoChaveNaoEncontradaException;
import atividade_biblioteca.services.generic.GenericDAO;

public class EmprestimoDAO extends GenericDAO<Emprestimo, Long> implements IEmprestimoDAO {

	public EmprestimoDAO() {
		super(Emprestimo.class);
	}

	@Override
	public void finalizarEmprestimo(Emprestimo emprestimo) throws TipoChaveNaoEncontradaException, DAOException {
		super.alterar(emprestimo);
	}

	@Override
	public void cancelarEmprestimo(Emprestimo emprestimo) throws TipoChaveNaoEncontradaException, DAOException {
		super.alterar(emprestimo);
	}

	@Override
	public void excluir(Emprestimo entity) throws DAOException {
		throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
	}

	@Override
	public Emprestimo cadastrar(Emprestimo entity) throws TipoChaveNaoEncontradaException, DAOException {
		try {
			entity.getItens().forEach(item -> {
    		Livro livroJpa = entityManager.merge(item.getLivro());
    		item.setLivro(livroJpa);
    		item.setEmprestimo(entity);
							});
			Aluno Aluno = entityManager.merge(entity.getAluno());
			entity.setAluno(Aluno);
			entityManager.persist(entity);
			
			return entity;
		} catch (Exception e) {
			throw new DAOException("ERRO SALVANDO VENDA ", e);
		}
		
	}

	@Override
	public Emprestimo consultarComCollection(Long id) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Emprestimo> query = builder.createQuery(Emprestimo.class);
		Root<Emprestimo> root = query.from(Emprestimo.class);
		root.fetch("aluno");
		root.fetch("livros");
		query.select(root).where(builder.equal(root.get("id"), id));
		TypedQuery<Emprestimo> tpQuery = 
				entityManager.createQuery(query);
		Emprestimo emprestimo = tpQuery.getSingleResult(); 
		return emprestimo;
	}

	@Override
public boolean existeLivroEmprestado(Long idLivro) {

    String jpql = "SELECT COUNT(ei) " +
              "FROM EmprestimoItem ei " +
              "WHERE ei.livro.id = :idLivro " +
              "AND ei.dataDevolucao IS NULL";

    Long count = entityManager
            .createQuery(jpql, Long.class)
            .setParameter("idLivro", idLivro)
            .getSingleResult();

    return count > 0;
}
	
//	@Override
//	public Collection<Venda> buscarTodos() throws DAOException {
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT obj FROM ");
//		sb.append(this.persistenteClass.getSimpleName());
//		sb.append(" obj");
//		sb.append(" WHERE obj.status = :status");
//		TypedQuery<Venda> query = this.entityManager.createQuery(sb.toString(), Venda.class);
//		query.setParameter("status", Venda.Status.);
//		
//		List<T> list = 
//				entityManager.createQuery(getSelectSql(), this.persistenteClass).getResultList();
//		return list;
//	}

}
