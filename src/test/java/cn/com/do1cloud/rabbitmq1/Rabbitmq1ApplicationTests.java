package cn.com.do1cloud.rabbitmq1;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class Rabbitmq1ApplicationTests {
    private static final String QUEUE_XSM_NAME = "QUEUE_MQ";

    @Autowired
    private Connection connection;

    /*
        author:xsm
        time:2020.11.4 15:52
        function:发送一条消息给mq
     */
    @Test
    void Test() throws Exception{
        Channel channel = connection.createChannel();
        channel.queueDeclare("QUEUE_XSM_NAME",false,false,false,null);
        String message = "test";
        channel.basicPublish("",QUEUE_XSM_NAME,null,message.getBytes());
        channel.close();
        connection.close();
    }

    /*
        author:xsm
        time:2020.11.4 15:52
        function:接收一条消息给mq
     */
    @Test
    void Direct_Get_MQ() throws Exception{
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("directLogs",BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,"directLogs","jay");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag,false);
            }
        };
        GetResponse getResponse = channel.basicGet(QUEUE_XSM_NAME, false);
        if(getResponse == null){
            System.out.println("没有消息");
        }else{
            AMQP.BasicProperties props = getResponse.getProps();
            byte[] body = getResponse.getBody();
            long deliverTag = getResponse.getEnvelope().getDeliveryTag();
            channel.basicAck(deliverTag,false);
        }
        channel.basicConsume(queueName,false,consumer);
        channel.close();
        connection.close();
    }

}
