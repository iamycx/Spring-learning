package com.iamycx.resource;


import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileSourceExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String filePath = "E:/Myecplise/Workspaces/spring-ioc/WebRoot/WEB-INF/classes/conf/file1.txt";
			
			Resource res1 = new FileSystemResource(filePath);
			Resource res2 = new ClassPathResource("conf/file1.txt");
			
			InputStream ins1 = res1.getInputStream();
			InputStream ins2 = res2.getInputStream();
			
			System.out.println("res1:"+res1.getFilename());
			System.out.println("res2:"+res2.getFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
