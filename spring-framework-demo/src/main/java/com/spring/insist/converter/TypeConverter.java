package com.spring.insist.converter;

public interface TypeConverter {

	boolean isType(Class<?> clazz);
	
	Object convert(String source);
}
