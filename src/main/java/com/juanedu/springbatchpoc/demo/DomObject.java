package com.juanedu.springbatchpoc.demo;

import lombok.Data;

@Data
public class DomObject {

	private String clave;
	private String datos;
	private Integer delay;

	@Override
	public String toString() {
		return "Registro {" +
				"clave=" + clave +
				", datos=" + datos +
				", delay=" + delay + '}';
	}
}
