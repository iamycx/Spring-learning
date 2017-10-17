package com.iamycx.resource;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

public class EncodedResourceExample {

	/**
	 * @param args
	 * @throws IOException 
	 * 读取资源的内容
	 */
	public static void main(String[] args) throws IOException {
		Resource res = new ClassPathResource("conf/file1.txt");
		EncodedResource encRes = new EncodedResource(res, "UTF-8");
		String content = FileCopyUtils.copyToString(encRes.getReader());
		System.out.println(content);
	}

}
