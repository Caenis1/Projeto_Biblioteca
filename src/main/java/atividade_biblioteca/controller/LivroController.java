package atividade_biblioteca.controller;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.service.ILivroService;

@Named
@ViewScoped
public class LivroController implements Serializable {

	private static final long serialVersionUID = 367088063926303823L;
	
	private Livro livro;
	
	private Collection<Livro> livros;
	
	@Inject
	private ILivroService livroService;
	
	private Boolean isUpdate;
	
	@PostConstruct
    public void init() {
		try {
			this.isUpdate = false;
			this.livro = new Livro();
			this.livros = livroService.buscarTodos();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar listar os produtos"));
		}
	}
	
	public void cancel() {
		try {
			this.isUpdate = false;
			this.livro = new Livro();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cancelar ação"));
		}
		
    } 
	
	public void edit(Livro livro) {
		try {
			this.isUpdate = true;
			this.livro = livro;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar editar o livro   "));
		}
		
    } 
	
	public void delete(Livro livro) {
		try {
			livroService.excluir(livro);
			livros.remove(livro);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar excluir o livro"));
		}
		
    } 
	
	public void add() {
		try {
			livroService.cadastrar(livro);
			this.livros = livroService.buscarTodos();
			this.livro = new Livro();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar criar o livro"));
		}
		
        
    }

    public void update() {
    	try {
    		livroService.alterar(this.livro);
			cancel();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Livro Atualizado com sucesso"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar atualizar o produto"));
		}
        
    }
    
    public String voltarTelaInicial() {
		return "/index.xhtml"; 
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Collection<Livro> getLivros() {
		return livros;
	}

	public void setLivros(Collection<Livro> livros) {
		this.livros = livros;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
    
    

}
