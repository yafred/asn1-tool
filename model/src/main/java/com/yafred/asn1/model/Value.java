package com.yafred.asn1.model;

abstract public class Value  {
    /**
     * Type defining this value.
     */
    private Type type = null;

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isBinaryValue() {
        return false;
    }

    public boolean isBooleanValue() {
        return false;
    }

    public boolean isCharacterStringValue() {
        return false;
    }

    public boolean isChoiceValue() {
        return false;
    }

    public boolean isHexaValue() {
        return false;
    }

    public boolean isIntegerValue() {
        return false;
    }

    public boolean isNamedValueList() {
        return false;
    }

    public boolean isNullValue() {
        return false;
    }

    public boolean isObjectIdentifierValue() {
        return false;
    }

    public boolean isValueReference() {
        return false;
    }

    public boolean isValueList() {
        return false;
    }
}
