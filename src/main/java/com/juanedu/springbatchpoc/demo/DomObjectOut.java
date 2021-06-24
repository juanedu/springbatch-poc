package com.juanedu.springbatchpoc.demo;

import java.util.Date;

import lombok.Data;

@Data
public class DomObjectOut extends DomObjectIn {

	private Date timestamp;
	private String worker;

	@Override
	public String toString() {
		return "Registro {" +
				"clave=" + clave +
				", datos=" + datos +
				", delay=" + delay +
				", timestamp=" + timestamp +
				", worker=" + worker + '}';
	}
}
