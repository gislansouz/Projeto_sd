package com.restaurante.servidor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Despachante {

    public String SelecionarEsqueleto(String objetoName, String methodName, String params) throws Exception {
        String result=null;
        try {
        // Instanciar o objeto do serviço dinamicamente
        Class<?> serviceClass = Class.forName(objetoName);
        Object service = serviceClass.getDeclaredConstructor().newInstance();

        // Encontrar o método usando reflexão
        Method method = serviceClass.getMethod(methodName, String.class);

        // Invocar o método e retornar o resultado
        result = (String)method.invoke(service, params);

        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
        return result;
    }
}