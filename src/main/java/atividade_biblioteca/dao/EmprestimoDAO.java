package atividade_biblioteca.dao;

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
			entity.getProdutos().forEach(prod -> {
				Produto prodJpa = entityManager.merge(prod.getProduto());
				prod.setProduto(prodJpa);
			});
			Aluno Aluno = entityManager.merge(entity.getCliente());
			entity.setCliente(Aluno);
			entityManager.persist(entity);
//			entityManager.getTransaction().commit();
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
