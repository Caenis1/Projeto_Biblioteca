package atividade_biblioteca.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_EMPRESTIMO")
public class Emprestimo {

    public enum Status {
        INICIADO, CONCLUIDO, CANCELADO;

        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emprestimo_seq")
    @SequenceGenerator(name = "emprestimo_seq", sequenceName = "sq_emprestimo", allocationSize = 1)
    private Long id;

    @Column(name = "CODIGO", nullable = false, unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_aluno_fk",
            foreignKey = @ForeignKey(name = "fk_emprestimo_aluno"),
            nullable = false)
    private Aluno aluno;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmprestimoItem> itens;

    @Column(name = "DATA_EMPRESTIMO", nullable = false)
    private Instant dataEmprestimo;

    @Column(name = "DATA_DEVOLUCAO")
    private Instant dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    public Emprestimo() {
        this.itens = new HashSet<>();
        this.status = Status.INICIADO;
        this.dataEmprestimo = Instant.now();
    }


    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }


    public Set<EmprestimoItem> getItens() {
        return itens;
    }

    public Instant getDataEmprestimo() {
        return dataEmprestimo;
    }

    public Instant getDataDevolucao() {
        return dataDevolucao;
    }

    public Status getStatus() {
        return status;
    }


    public void adicionarLivro(Livro livro) {
        validarStatus();

        Optional<EmprestimoItem> op =
                itens.stream().filter(i -> i.contemLivro(livro)).findAny();

        if (op.isEmpty()) {
            EmprestimoItem item = new EmprestimoItem(livro);
            item.setEmprestimo(this);
            itens.add(item);
        }
    }

    public void removerLivro(Livro livro) {
        validarStatus();

        itens.removeIf(i -> i.contemLivro(livro));
    }

    public void removerTodosLivros() {
        validarStatus();
        itens.clear();
    }

    public Integer getQuantidadeLivros() {
        return itens.size();
    }

    public void finalizarEmprestimo() {
        if (itens.isEmpty()) {
            throw new RuntimeException("Não é possível finalizar um empréstimo sem livros");
        }
        this.status = Status.CONCLUIDO;
        this.dataDevolucao = Instant.now();
    }

    public void cancelarEmprestimo() {
        this.status = Status.CANCELADO;
    }

    private void validarStatus() {
        if (this.status == Status.CONCLUIDO) {
            throw new UnsupportedOperationException("EMPRÉSTIMO JÁ FINALIZADO");
        }
    }
}
