package com.yafred.asn1.model;

public class NamedType extends Component {
    private String name = null;
    private Type type = null;
    private boolean isOptional = false;
    private Value defaultValue = null;
    private TokenLocation tokenLocation = null;
    
    public NamedType(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public NamedType(String name, Type type, boolean isOptional) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
    }

    public NamedType(String name, Type type, boolean isOptional, Value defaultValue) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
        this.defaultValue = defaultValue;
    }
    
    public NamedType copy() {
    	return new NamedType(this.name, this.type, this.isOptional, this.defaultValue);
    }

   public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public Value getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Value defaultValue) {
		this.defaultValue = defaultValue;
	}

	public TokenLocation getTokenLocation() {
		return tokenLocation;
	}

	public void setTokenLocation(TokenLocation tokenLocation) {
		this.tokenLocation = tokenLocation;
	}

	@Override
	public boolean isNamedType() { 
		return true; 
	}
}
