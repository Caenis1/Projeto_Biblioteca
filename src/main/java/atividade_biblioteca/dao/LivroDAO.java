package atividade_biblioteca.dao;

public class LivroDAO extends GenericDAO<Livro, String> implements ILivroDAO {

	public LivroDAO() {
		super(Livro.class);
	}

	@Override
	public List<Livro> filtrarLivros(String query) {
		TypedQuery<Livro> tpQuery = 
				this.entityManager.createNamedQuery("Livro.findByTitulo", this.persistenteClass);
		tpQuery.setParameter("titulo", "%" + query + "%");
        return tpQuery.getResultList();
	}

}
