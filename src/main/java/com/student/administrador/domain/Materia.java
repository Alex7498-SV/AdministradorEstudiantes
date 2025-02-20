package com.student.administrador.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(schema="public", name="materia")
public class Materia {
	
	@Id
	@Column(name="idMateria")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idMateria;
	
	@Column(name="nombre")
	@NotEmpty(message="El campo Nombre no puede ir vacio")
	@Size(max=50, message="El campo Nombre no debe tener mas de 50 caracteres")
	private String nombre;
	
	@Column(name="estado")
	private Boolean estado;

	public Integer getIdMateria() {
		return idMateria;
	}
	public void setIdMateria(Integer idMateria) {
		this.idMateria = idMateria;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getEstadoDelegate() {
        if(this.estado == null)return "";
        else {
            return estado == true ?"Activo":"Inactivo";
        }
	}
}
