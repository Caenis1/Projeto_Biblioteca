package atividade_biblioteca.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.services.generic.GenericDAO;

public class LivroDAO 
    extends GenericDAO<Livro, Long> 
    implements ILivroDAO {
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
