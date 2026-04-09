package atividade_biblioteca.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_EMPRESTIMO_ITEM")
public class EmprestimoItem{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_item_seq")
    @SequenceGenerator(name = "emp_item_seq", sequenceName = "sq_emp_item", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_livro_fk", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "id_emprestimo_fk", nullable = false)
    private Emprestimo emprestimo;

    public EmprestimoItem() {
    }

    public EmprestimoItem(Livro livro) {
        this.livro = livro;
    }


    public Long getId() {
        return id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }


    public void adicionarAoEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    
    public void removerDoEmprestimo() {
        this.emprestimo = null;
    }

    
    public boolean contemLivro(Livro livro) {
        return this.livro != null && this.livro.equals(livro);
    }

   
    public boolean isValido() {
        return this.livro != null;
    }
}
