package atividade_biblioteca.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_ALUNO")
@NamedQuery(name = "Aluno.findByNome", query = "SELECT a FROM Aluno a WHERE a.nome LIKE :nome") 
public class Aluno implements Persistente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="aluno_seq")
	@SequenceGenerator(name="aluno_seq", sequenceName="sq_aluno", initialValue = 1, allocationSize = 1)
	private Long id;
	
	@Column(name = "NOME", nullable = false, length = 50)
	private String nome;
	
	@Column(name = "CPF", nullable = false, unique = true)
    private Long cpf;
    
	@Column(name = "TEL", nullable = false)
    private Long tel;
	
	@Column(name = "EMAIL", nullable = false, length = 50)
    private String email;
    
    @Column(name = "TURMA", nullable = false, length = 50)
    private String turma;
    
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getCpf() {
		return cpf;
	}
	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
	public Long getTel() {
		return tel;
	}
	public void setTel(Long tel) {
		this.tel = tel;
	}
	public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	

	

}
