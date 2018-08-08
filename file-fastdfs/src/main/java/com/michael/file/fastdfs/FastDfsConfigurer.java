package com.michael.file.fastdfs;

import java.util.Properties;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastDfsConfigurer {

	@Value("${fastdfs.connect_timeout}")
	private String connect_timeout;
	@Value("${fastdfs.network_timeout}")
	private String network_timeout;
	@Value("${fastdfs.charset}")
	private String charset;
	@Value("${fastdfs.tracker_server}")
	private String tracker_server;

	@Bean
	public TrackerClient trackerClient() {
		Properties properties = new Properties();
		properties.setProperty("connect_timeout", connect_timeout);
		properties.setProperty("network_timeout", network_timeout);
		properties.setProperty("charset", charset);
		properties.setProperty("tracker_server", tracker_server);

		new ClientGlobal(properties);

		TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);

		return trackerClient;
	}

}
