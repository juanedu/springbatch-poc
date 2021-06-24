package com.juanedu.springbatchpoc.demo;

import lombok.Data;

@Data
public class DomObjectIn {

	protected String clave;
	protected String datos;
	protected Integer delay;

	@Override
	public String toString() {
		return "Registro {" +
				"clave=" + clave +
				", datos=" + datos +
				", delay=" + delay + '}';
	}
}
