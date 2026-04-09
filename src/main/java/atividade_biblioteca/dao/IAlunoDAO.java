package atividade_biblioteca.dao;

import java.util.List;

import atividade_biblioteca.domain.Aluno;
import atividade_biblioteca.services.generic.IGenericDAO;

public interface IAlunoDAO extends IGenericDAO<Aluno, Long>{

	List<Aluno> filtrarAlunos(String query);

}
