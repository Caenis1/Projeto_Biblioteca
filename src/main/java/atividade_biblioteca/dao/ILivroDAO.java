package atividade_biblioteca.dao;

import java.util.List;

import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.services.generic.IGenericDAO;

public interface ILivroDAO extends IGenericDAO<Livro, Long>{

	List<Livro> filtrarLivros(String query);

}
