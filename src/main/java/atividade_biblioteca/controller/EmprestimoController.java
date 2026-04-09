package atividade_biblioteca.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import atividade_biblioteca.domain.Aluno;
import atividade_biblioteca.domain.Emprestimo;
import atividade_biblioteca.domain.EmprestimoItem;
import atividade_biblioteca.domain.Livro;
import atividade_biblioteca.service.IAlunoService;
import atividade_biblioteca.service.IEmprestimoService;
import atividade_biblioteca.service.ILivroService;

@Named
@ViewScoped
public class EmprestimoController implements Serializable {

	private static final long serialVersionUID = -3508753726177740824L;
	
	private Emprestimo emprestimo;
	
	private Collection<Emprestimo> emprestimos;
	
	@Inject
	private IEmprestimoService emprestimoService;
	
	@Inject
	private IAlunoService clienteService;
	
	@Inject
	private ILivroService produtoService;
	
	private Boolean isUpdate;
	
	private LocalDate dataVenda;
	
	private Integer quantidadeProduto;
	
	private Set<EmprestimoItem> produtos;
	
	private Livro produtoSelecionado;
	
	private BigDecimal valorTotal; 
	
	@PostConstruct
    public void init() {
		try {
			this.isUpdate = false;
			this.emprestimo = new Emprestimo();
			this.produtos = new HashSet<>();
			this.emprestimos = emprestimoService.buscarTodos();
			this.valorTotal = BigDecimal.ZERO;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar listar as vendas"));
		}
	}
	
	public void cancel() {
		try {
			this.isUpdate = false;
			this.emprestimo = new Emprestimo();
			this.livros = new HashSet<>();
			this.valorTotal = BigDecimal.ZERO;
			this.dataVenda = null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cancelar ação"));
		}
		
    } 
	
	public void edit(Emprestimo emprestimo) {
		try {
			this.isUpdate = true;
			this.emprestimo = this.emprestimoService.consultarComCollection(emprestimo.getId());
			this.dataVenda = LocalDate.ofInstant(this.emprestimo.getDataVenda(), ZoneId.systemDefault());
			this.produtos = this.emprestimo.getProdutos();
			this.emprestimo.recalcularValorTotalVenda();
			this.valorTotal = this.emprestimo.getValorTotal();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar editar a venda"));
		}
		
    } 
	
	public void delete(Emprestimo emprestimo) {
		try {
			emprestimoService.cancelarEmprestimo(emprestimo);
			cancel();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cancelar a venda"));
		}
		
    } 
	
	public void finalizar(Emprestimo emprestimo) {
		try {
			emprestimoService.finalizarEmprestimo(emprestimo);
			cancel();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar finalizar a venda"));
		}
		
    } 
	
	public void add() {
		try {
			emprestimo.setDataVenda(dataVenda.atStartOfDay(ZoneId.systemDefault()).toInstant());
			emprestimoService.cadastrar(emprestimo);
			this.emprestimos = emprestimoService.buscarTodos();
			cancel();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cadastrar a venda"));
		}
    }
	
	public void update() {
    	try {
    		emprestimoService.alterar(this.emprestimo);
    		this.emprestimos = emprestimoService.buscarTodos();
			cancel();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Venda atualiada com sucesso"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar atualizar a venda"));
		}
        
    }
	
	public void adicionarProduto() {
		Optional<EmprestimoItem> prodOp = 
				this.emprestimo.getProdutos().stream().filter(prodF -> prodF.getProduto().getCodigo().equals(this.produtoSelecionado.getCodigo())).findFirst();

		if (prodOp.isPresent()) {
			EmprestimoItem prod = prodOp.get();
			prod.adicionar(this.quantidadeProduto);
		} else {
			EmprestimoItem prod = new EmprestimoItem();
			prod.setProduto(this.produtoSelecionado);
			prod.adicionar(this.quantidadeProduto);
			prod.setEmprestimo(this.emprestimo);
			this.emprestimo.getProdutos().add(prod);
		}
		this.emprestimo.recalcularValorTotalVenda();
		this.produtos = this.emprestimo.getProdutos();
		this.valorTotal = this.emprestimo.getValorTotal();
	}
	
	public void removerProduto() {
		Optional<EmprestimoItem> prodOp = 
				this.emprestimo.getProdutos().stream().filter(prodF -> prodF.getProduto().getCodigo().equals(this.produtoSelecionado.getCodigo())).findFirst();

		if (prodOp.isPresent()) {
			EmprestimoItem prod = prodOp.get();
			prod.remover(this.quantidadeProduto);
			if (prod.getQuantidade() == 0 || prod.getQuantidade() < 0) {
				this.emprestimo.getProdutos().remove(prod);
			}
			this.emprestimo.recalcularValorTotalVenda();
			this.produtos = this.emprestimo.getProdutos();
			this.valorTotal = this.emprestimo.getValorTotal();
		}
		
	}
	
	public void removerProduto(EmprestimoItem produto) {
		
		this.emprestimo.getProdutos().remove(produto);
		this.emprestimo.recalcularValorTotalVenda();
		this.produtos = this.emprestimo.getProdutos();
		this.valorTotal = this.emprestimo.getValorTotal();
	}
	
	public void onRowEdit(RowEditEvent<EmprestimoItem> event) {
		EmprestimoItem prod = (EmprestimoItem) event.getObject();
		adicionarOuRemoverProduto(prod);
    }

    public void onRowCancel(RowEditEvent<EmprestimoItem> event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", String.valueOf(event.getObject().getId()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void adicionarOuRemoverProduto(EmprestimoItem prod) {
    	if (prod.getQuantidade() != this.quantidadeProduto) {
    		int quantidade =  this.quantidadeProduto - prod.getQuantidade();
    		if (quantidade > 0) {
    			prod.adicionar(quantidade);
    		} else {
    			this.produtos.remove(prod);
    		}
    		this.valorTotal = BigDecimal.ZERO;
    		this.produtos.forEach(pro -> {
    			this.valorTotal = this.valorTotal.add(pro.getValorTotal());
    		});
    	}
    }
	
	public List<Aluno> filtrarClientes(String query) {
		return this.alunoService.filtrarClientes(query);
	}
	
	public List<Livros> filtrarProdutos(String query) {
		return this.produtoService.filtrarProdutos(query);
	}
    
    public String voltarTelaInicial() {
		return "/index.xhtml"; 
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Collection<Emprestimo> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(Collection<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public LocalDate getDataEmprestimo() {
		return dataVenda;
	}

	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public Set<EmprestimoItem> getLivros() {
		return livro;
	}

	public void setLivros(Set<EmprestimoItem> livros) {
		this.livro = livros;
	}

	public Integer getEmprestmoItens() {
		return EmprestimoItens;
	}

	public void setEmprestimoItem(Integer emprestimoItem) {
		this.emprestimoItem = emprestimoItem;
	}

	public Livro getLivroSelecionado() {
		return livroSelecionado;
	}

	public void setLivroSelecionado(Livro livroSelecionado) {
		this.livroSelecionado = livroSelecionado;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	
    

}
