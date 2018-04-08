package com.yafred.asn1.model;

public class DefinitiveObjectIdComponent {
    private String name = null;
    private Integer number = null;


    public DefinitiveObjectIdComponent(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
}
