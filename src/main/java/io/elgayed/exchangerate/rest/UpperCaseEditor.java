package io.elgayed.exchangerate.rest;

import java.beans.PropertyEditorSupport;

public class UpperCaseEditor extends PropertyEditorSupport{
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    	setValue(text.toUpperCase());
    }
}