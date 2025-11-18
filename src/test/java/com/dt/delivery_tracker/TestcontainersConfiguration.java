package com.dt.delivery_tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	OracleContainer oracleFreeContainer(@Value("${testcontainers.oracle.image:gvenzl/oracle-xe:21-slim}") String dockerImage) {
		DockerImageName myImage = DockerImageName.parse(dockerImage)
				.asCompatibleSubstituteFor("gvenzl/oracle-free");
		return new OracleContainer(myImage);
	}

}
