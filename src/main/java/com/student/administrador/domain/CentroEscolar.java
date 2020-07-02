package com.student.administrador.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(schema="public", name="centroEscolar")
public class CentroEscolar {
	
	@Id
	@Column(name="idEscolar")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idEscolar;
	
	@Column(name="nombre")
	@NotEmpty(message="El campo Nombre no puede ir vacio")
	@Size(max=30, message="El campo Nombre no debe tener mas de 30 caracteres")
	private String nombre;
	
	@Column(name="estado")
	private Boolean estado;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idMunicipio")
	private Municipio municipio;
	
	@OneToMany(fetch=FetchType.EAGER)
	private List<Estudiante> estudiantes;

	public Integer getIdEscolar() {
		return idEscolar;
	}

	public void setIdEscolar(Integer idEscolar) {
		this.idEscolar = idEscolar;
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

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public List<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(List<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}
	
	public String getEstadoDelegate() {
        if(this.estado == null)return "";
        else {
            return estado == true ?"Activo":"Inactivo";
        }
	}
}
