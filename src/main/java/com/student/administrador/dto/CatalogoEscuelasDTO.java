package com.student.administrador.dto;

public class CatalogoEscuelasDTO {
	private Integer Codigo;
	private String Descripcion;
	private Boolean Estado;
	private String municipio;
	
	public Integer getCodigo() {
		return Codigo;
	}
	public void setCodigo(Integer codigo) {
		Codigo = codigo;
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	public Boolean getEstado() {
		return Estado;
	}
	public void setEstado(Boolean estado) {
		Estado = estado;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getEstadoDelegate() {
        if(this.Estado == null)return "";
        else {
            return Estado == true ?"Activo":"Inactivo";
        }
	}
}
