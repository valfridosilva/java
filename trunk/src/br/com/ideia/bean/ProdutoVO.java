package br.com.ideia.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "produto")
@Table(name = "TB_PRODUTO")
public class ProdutoVO implements FlatWormBean {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Long codigo;
	private String descricao;
	private Double valor;
	private CategoriaVO categoria;
	private FabricanteVO fabricante;

	@Id
	@GeneratedValue
	@Column(name = "id_produto", insertable = false, updatable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "codigo")
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Column(name = "descricao", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "valor", scale = 2)
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "id_categoria")
	public CategoriaVO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaVO categoria) {
		this.categoria = categoria;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "id_fabricante")
	public FabricanteVO getFabricante() {
		return fabricante;
	}

	public void setFabricante(FabricanteVO fabricante) {
		this.fabricante = fabricante;
	}

}
