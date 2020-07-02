package com.student.administrador.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.validation.constraints.Size;

@Entity
@Table(schema="public", name="usuario")
public class Usuario {
	
	@Id
	@Column(name="idUsuario")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idUsuario;
	
	@Column(name="nombre")
	@NotEmpty(message="El campo Nombre no puede ir vacio")
	@Size(max=30, message="El campo Nombre no debe tener mas de 30 caracteres")
	private String nombre;
	
	@Column(name="apellido")
	@NotEmpty(message="El campo Apellido no puede ir vacio")
	@Size(max=30, message="El campo Apellido no debe tener mas de 30 caracteres")
	private String apellido;
	
	@Column(name="fecha_nacimiento")
	//@NotEmpty(message="El campo Fecha de Nacimiento no puede ir vacio")
	private Date fechaNac;
	
	@Column(name="direccion")
	@NotEmpty(message="El campo Direccion no puede ir vacio")
	@Size(max=75, message="El campo Direccion no debe tener mas de 75 caracteres")
	private String direccion;
	
	@Column(name="usuario")
	@NotEmpty(message="El campo Usuario no puede ir vacio")
	@Size(max=15, message="El campo Usuario no debe tener mas de 15 caracteres")
	private String usuario;
	
	@Column(name="contra")
	@NotEmpty(message="El campo Contraseña no puede ir vacio")
	@Size(max=15, message="El campo Contraseña no debe tener mas de 10 caracteres")
	private String contra;
	
	@Column(name="administrador")
	private Boolean administrador;
	
	@Column(name="estado")
	private Boolean estado;
	
	@Column(name="sesion")
	private Boolean sesion;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idMunicipio")
	private Municipio idMunicipio;

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(String fechaNac) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = df.parse(fechaNac);
		this.fechaNac = date;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContra() {
		return contra;
	}

	public void setContra(String contra) {
		this.contra = contra;
	}

	public Boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Boolean getSesion() {
		return sesion;
	}

	public void setSesion(Boolean sesion) {
		this.sesion = sesion;
	}

	public Municipio getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Municipio idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	
	public String getEstadoDelegate() {
        if(this.estado == null)return "";
        else {
            return estado == true ?"Activa":"Inactiva";
        }
	}
	
	public String getSesionDelegate() {
        if(this.sesion == null)return "";
        else {
            return sesion == true ?"Activo":"Inactivo";
        }
	}
}
