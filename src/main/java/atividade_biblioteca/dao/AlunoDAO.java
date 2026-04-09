package atividade_biblioteca.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import atividade_biblioteca.domain.Aluno;
import atividade_biblioteca.services.generic.GenericDAO;

public class AlunoDAO extends GenericDAO<Aluno, Long> implements IAlunoDAO {

	public AlunoDAO() {
		super(Aluno.class);
	}

	@Override
	public List<Aluno> filtrarAlunos(String query) {
		TypedQuery<Aluno> tpQuery = 
				this.entityManager.createNamedQuery("Aluno.findByNome", this.persistenteClass);
		tpQuery.setParameter("nome", "%" + query + "%");
        return tpQuery.getResultList();
		
	}

}
