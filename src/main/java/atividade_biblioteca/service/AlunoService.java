package atividade_biblioteca.service;

import atividade_biblioteca.dao.AlunoDAO;
import atividade_biblioteca.domain.Aluno;
import atividade_biblioteca.service.generic.GenericService;

public class AlunoService extends GenericService<Aluno, Long>  implements IAlunoService {

    public AlunoService(AlunoDAO dao) {
        super(dao);
    }
}
