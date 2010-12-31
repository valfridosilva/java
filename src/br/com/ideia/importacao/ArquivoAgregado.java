package br.com.ideia.importacao;

import java.util.List;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.bean.FlatWormBean;
import br.com.ideia.bean.ProdutoVO;
@FlatWormAgregate
public class ArquivoAgregado implements FlatWormBean{

	private static final long serialVersionUID = 1L;	
	private List<ClienteVO> clientes;	
	private List<ProdutoVO> produtos;
	
	@FlatWorm(beanName="cliente",recordName="cliente")
	public List<ClienteVO> getClientes() {
		return clientes;
	}
	public void setClientes(List<ClienteVO> clientes) {
		this.clientes = clientes;
	}
	@FlatWorm(beanName = "produto", recordName = "produto")
	public List<ProdutoVO> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<ProdutoVO> produtos) {
		this.produtos = produtos;
	}	
}
