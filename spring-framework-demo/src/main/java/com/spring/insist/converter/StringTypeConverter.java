package com.spring.insist.converter;

public class StringTypeConverter implements TypeConverter {

	@Override
	public boolean isType(Class<?> clazz) {
		return clazz == String.class;
	}
	
	@Override
	public String convert(String source) {
		return source;
	}

}
