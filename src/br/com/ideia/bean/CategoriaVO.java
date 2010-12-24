package br.com.ideia.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.ideia.importacao.FlatWorm;

@Entity(name="categoria")
@Table(name="TB_CATEGORIA")
@FlatWorm(beanName="categoria",recordName="header")
public class CategoriaVO implements FlatWormBean{

	private static final long serialVersionUID = 6089604184883673470L;
	
	private Integer id;
	private String descricao;
	
	@Id
	@GeneratedValue
	@Column(name="id_categoria",insertable=false,updatable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoriaVO other = (CategoriaVO) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}	
	
	@Override
	public String toString() {		
		return this.descricao;
	}
}
