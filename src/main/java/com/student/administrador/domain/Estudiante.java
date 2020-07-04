package com.student.administrador.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(schema="public", name="estudiante")
public class Estudiante {
	
	@Id
	@Column(name="idEstudiante")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idEstudiante;
	
	@Column(name="nombres")
	@NotEmpty(message="El campo Nombres no puede ir vacio")
	@Size(max=60, message="El campo Nombres no debe tener mas de 60 caracteres")
	private String nombres;
	
	@Column(name="apellidos")
	@NotEmpty(message="El campo Apellidos no puede ir vacio")
	@Size(max=60, message="El campo Apellidos no debe tener mas de 60 caracteres")
	private String apellidos;
	
	@Column(name="carnet")
	@NotEmpty(message="El campo Carnet no puede ir vacio")
	@Pattern(regexp="^$|[a-zA-Z0-9]{9}", message="Carnet no valido")
	private String carnet;
	
	@Column(name="fecha_nacimiento")
	//@NotEmpty(message="El campo Fecha de nacimiento no puede ir vacio")
	private Date fechaNac;
	
	@Column(name="direccion")
	@NotEmpty(message="El campo Direccion no puede ir vacio")
	@Size(max=50, message="El campo Direccion no debe tener mas de 50 caracteres")
	private String direccion;
	
	@Column(name="telefono_fijo")
	@NotEmpty(message="El campo Telefono Fijo no puede ir vacio")
	@Pattern(regexp="^$|[0-9]{8}", message="Numero de telefono no valido")
	private String tel_fijo;
	
	@Column(name="telefono_movil")
	@NotEmpty(message="El campo Telefono Movil no puede ir vacio")
	@Pattern(regexp="^$|[0-9]{8}", message="Numero de telefono no valido")
	private String tel_movil;
	
	@Column(name="nombre_padre")
	@NotEmpty(message="El campo Nombre del Padre no puede ir vacio")
	@Size(max=30, message="El campo Nombre del Padre no debe tener mas de 30 caracteres")
	private String nombrePadre;
	
	@Column(name="nombre_madre")
	@NotEmpty(message="El campo Nombre de la Madre no puede ir vacio")
	@Size(max=30, message="El campo Nombre de la Madre no debe tener mas de 30 caracteres")
	private String nombreMadre;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEscolar")
	private CentroEscolar centroescolar;

	public Integer getIdEstudiante() {
		return idEstudiante;
	}

	public void setIdEstudiante(Integer idEstudiante) {
		this.idEstudiante = idEstudiante;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCarnet() {
		return carnet;
	}

	public void setCarnet(String carnet) {
		this.carnet = carnet;
	}

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTel_fijo() {
		return tel_fijo;
	}

	public void setTel_fijo(String tel_fijo) {
		this.tel_fijo = tel_fijo;
	}

	public String getTel_movil() {
		return tel_movil;
	}

	public void setTel_movil(String tel_movil) {
		this.tel_movil = tel_movil;
	}

	public String getNombrePadre() {
		return nombrePadre;
	}

	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}

	public String getNombreMadre() {
		return nombreMadre;
	}

	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}

	public CentroEscolar getcentroEscolar() {
		return centroescolar;
	}

	public void setcentroEscolar(CentroEscolar centroescolar) {
		this.centroescolar = centroescolar;
	}
}
