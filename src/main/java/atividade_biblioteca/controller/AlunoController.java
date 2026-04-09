package atividade_biblioteca.controller;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import atividade_biblioteca.domain.Aluno;
import atividade_biblioteca.service.IAlunoService;

@Named
@ViewScoped
public class AlunoController implements Serializable {

	private static final long serialVersionUID = 8030245985235567808L;
	
	private Aluno aluno;
	
	private Collection<Aluno> alunos;
	
	@Inject
	private IAlunoService alunoService;
	
	private Boolean isUpdate;
	
	private String cpfMask;
	
	private String telMask;
	
	@PostConstruct
    public void init() {
		try {
			this.isUpdate = false;
			this.aluno = new Aluno();
			this.alunos = alunoService.buscarTodos();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar listar os clientes"));
		}
	}
	
	public void cancel() {
		try {
			this.isUpdate = false;
			this.aluno = new Aluno();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cancelar ação"));
		}
		
    } 
	
	public void edit(Aluno aluno) {
		try {
			this.isUpdate = true;
			this.aluno = aluno;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar excluir o cliente"));
		}
		
    } 
	
	public void delete(Aluno aluno) {
		try {
			alunoService.excluir(aluno);
			alunos.remove(aluno);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar excluir o cliente"));
		}
		
    } 
	
	public void add() {
		try {
			removerCaracteresInvalidos();
			limparCampos();
			alunoService.cadastrar(aluno);
			this.alunos = alunoService.buscarTodos();
			this.aluno = new Aluno();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar criar o cliente"));
		}
		
        
    }

    private void removerCaracteresInvalidos() {
    	Long cpf = Long.valueOf(ReplaceUtils.replace(getCpfMask(), ".", "-"));
    	this.aluno.setCpf(cpf);
    	
    	Long tel = Long.valueOf(ReplaceUtils.replace(getTelMask(), "(", ")", " ", "-"));
    	this.aluno.setTel(tel);
	}
    
    private void limparCampos() {
    	setCpfMask(null);
    	setTelMask(null);
    }

	public void update() {
    	try {
    		removerCaracteresInvalidos();
			alunoService.alterar(this.aluno);
			cancel();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Aluno Atualizado com sucesso"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar atualizar o cliente"));
		}
        
    }
	
	public String voltarTelaInicial() {
		return "/index.xhtml"; 
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Collection<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(Collection<Aluno> alunos) {
		this.alunos = alunos;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getCpfMask() {
		return cpfMask;
	}

	public void setCpfMask(String cpfMask) {
		this.cpfMask = cpfMask;
	}

	public String getTelMask() {
		return telMask;
	}

	public void setTelMask(String telMask) {
		this.telMask = telMask;
	}
	
	

}
