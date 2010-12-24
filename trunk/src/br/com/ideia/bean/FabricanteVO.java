package br.com.ideia.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.ideia.importacao.FlatWorm;

@Entity(name="fabricante")
@Table(name="TB_FABRICANTE")
@FlatWorm(beanName="fabricante",recordName="header")
public class FabricanteVO implements FlatWormBean{

	private static final long serialVersionUID = 2524649977034633572L;
	
	private Integer id;
	private String descricao;
	
	@Id
	@GeneratedValue
	@Column(name="id_fabricante",insertable=false,updatable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="descricao")
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
		FabricanteVO other = (FabricanteVO) obj;
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
