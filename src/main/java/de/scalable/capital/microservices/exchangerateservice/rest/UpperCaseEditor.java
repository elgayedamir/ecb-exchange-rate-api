package de.scalable.capital.microservices.exchangerateservice.rest;

import java.beans.PropertyEditorSupport;

public class UpperCaseEditor extends PropertyEditorSupport{
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    	setValue(text.toUpperCase()); //TODO not null safe
    }
}