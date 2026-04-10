package atividade_biblioteca.service;

import atividade_biblioteca.dao.LivroDAO;
import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.service.generic.GenericService;

public class LivroService 
    extends GenericService<Livro, Long> 
    implements ILivroService {

    public LivroService(LivroDAO dao) {
        super(dao);
    }
}