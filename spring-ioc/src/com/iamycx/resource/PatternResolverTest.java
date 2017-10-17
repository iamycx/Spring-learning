package com.iamycx.resource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class PatternResolverTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resources[] = resolver
				.getResources("classpath*:com/iamycx/**/*.xml");
		for (Resource resource : resources) {
			System.out.println(resource.getDescription());
		}
	}

}
