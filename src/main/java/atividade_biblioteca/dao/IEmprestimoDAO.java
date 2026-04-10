package atividade_biblioteca.dao;

import atividade_biblioteca.domain.Emprestimo;
import atividade_biblioteca.exceptions.DAOException;
import atividade_biblioteca.exceptions.TipoChaveNaoEncontradaException;
import atividade_biblioteca.services.generic.IGenericDAO;

public interface IEmprestimoDAO extends IGenericDAO<Emprestimo, Long>{

	public void finalizarEmprestimo(Emprestimo emprestimo) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void cancelarEmprestimo(Emprestimo emprestimo) throws TipoChaveNaoEncontradaException, DAOException;
	
	/**
	 * Usando este método para evitar a exception org.hibernate.LazyInitializationException
	 * Ele busca todos os dados de objetos que tenham colletion pois a mesma por default é lazy
	 * Mas você pode configurar a propriedade da collection como fetch = FetchType.EAGER na anotação @OneToMany e usar o consultar genérico normal
	 * 
	 * OBS: Não é uma boa prática utiliar FetchType.EAGER pois ele sempre irá trazer todos os objetos da collection
	 * mesmo sem precisar utilizar.
	 * 
	 * 
	 * @see VendaJpa produtos
	 * 
	 * @param id
	 * @return
	 */
	public Emprestimo consultarComCollection(Long id);

	boolean existeLivroEmprestado(Long idLivro);

}
