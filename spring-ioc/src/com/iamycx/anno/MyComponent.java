package com.iamycx.anno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {
    
	@Autowired(required=false)
	private List<Plugin> plugins;

	public List<Plugin> getPlugins() {
		return plugins;
	}
}
