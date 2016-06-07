package com.alexmochalov.files;

import java.util.ArrayList;

public class NamedArrayList{
	class Element{
		String name;
		Object value;
	}
    private ArrayList<Element> array = new ArrayList<Element>();
    
    public void add(String name, Object value){
    	Element element = new Element();
    	element.name =  name;
    	element.value =  value;
    	array.add( element);
    }
}
