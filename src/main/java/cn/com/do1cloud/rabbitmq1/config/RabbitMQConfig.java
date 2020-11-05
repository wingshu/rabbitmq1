package cn.com.do1cloud.rabbitmq1.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Connection getConnection() throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setHost("localhost");
        System.out.println(connectionFactory.getPassword() + " " + connectionFactory.getUsername() + " " + connectionFactory.getHost()+" "+connectionFactory.getPort());
        Connection connection = connectionFactory.newConnection();
        return connection;
    }
}
