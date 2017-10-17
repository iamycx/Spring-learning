package com.iamycx.reflect;

public class ClassLoaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		System.out.println("current loader: "+loader);
		System.out.println("parent loader: "+loader.getParent());
		System.out.println("grandparent loader: "+loader.getParent().getParent());
	}

}
