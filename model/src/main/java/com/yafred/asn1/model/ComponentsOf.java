package com.yafred.asn1.model;

public class ComponentsOf extends Component {
    private Type foreignContainer = null;

    public ComponentsOf(Type type) {
        this.foreignContainer = type;
    }

    public Type getForeignContainer() {
		return foreignContainer;
	}

	public void setForeignContainer(Type foreignContainer) {
		this.foreignContainer = foreignContainer;
	}

	@Override
	public boolean isComponentsOf() {
        return true;
    }
}
