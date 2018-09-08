package org.springframework.cloud.stream.binder.mns;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import org.springframework.cloud.stream.binder.*;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MNSMessageChannelBinder implements Binder<MessageChannel, ConsumerProperties, ProducerProperties> {

    private final MNSProperties mnsProperties;

    public MNSMessageChannelBinder(MNSProperties mnsProperties) {
        this.mnsProperties = mnsProperties;
    }

    @Override
    public Binding<MessageChannel> bindConsumer(String name, String group, MessageChannel inputChannel, ConsumerProperties consumerProperties) {
        CloudAccount account = null;
        try{
            account = new CloudAccount(ServiceSettings.getMNSAccessKeyId(), ServiceSettings.getMNSAccessKeySecret(), ServiceSettings.getMNSAccountEndpoint());
        }catch (Exception e){
            try{
                account = new CloudAccount(mnsProperties.getAccesskeyid(), mnsProperties.getAccesskeysecret(), mnsProperties.getAccountendpoint());
            }catch (Exception ex){
                throw new RuntimeException("aliyun mns account info not found.");
            }
        }
        MNSClient client = account.getMNSClient();
        try {
            CloudQueue queue = client.getQueueRef(name);
            List<Message> messageList = new ArrayList<>();
            for(int i = 0; i < 10; ++i) {
                Message popMsg = queue.popMessage();
                if (popMsg != null) {
                    System.out.println("message handle: " + popMsg.getReceiptHandle());
                    System.out.println("message body: " + popMsg.getMessageBodyAsString());
                    System.out.println("message id: " + popMsg.getMessageId());
                    System.out.println("message dequeue count:" + popMsg.getDequeueCount());
                    queue.deleteMessage(popMsg.getReceiptHandle());
                    System.out.println("delete message successfully.\n");
                    messageList.add(popMsg);
                }
            }
            messageList.forEach(msg->{
                byte[] body = msg.getMessageBodyAsBytes();
                inputChannel.send(new GenericMessage<Object>(body));
            });
        } catch (ClientException var6) {
            System.out.println("Something wrong with the network connection between client and MNS service.Please check your network and DNS availablity.");
            var6.printStackTrace();
        } catch (ServiceException var7) {
            if (var7.getErrorCode().equals("QueueNotExist")) {
                System.out.println("Queue is not exist.Please create queue before use");
            } else if (var7.getErrorCode().equals("TimeExpired")) {
                System.out.println("The request is time expired. Please check your local machine timeclock");
            }
            var7.printStackTrace();
        } catch (Exception var8) {
            System.out.println("Unknown exception happened!");
            var8.printStackTrace();
        }
        return () -> {
            client.close();
        };
    }


    /**
     * 发送消息的Binder
     * @param name
     * @param outBoundChannel
     * @param producerProperties
     * @return
     */
    @Override
    public Binding<MessageChannel> bindProducer(String name, MessageChannel outBoundChannel, ProducerProperties producerProperties) {
        CloudAccount account = null;
        try{
            account = new CloudAccount(ServiceSettings.getMNSAccessKeyId(), ServiceSettings.getMNSAccessKeySecret(), ServiceSettings.getMNSAccountEndpoint());
        }catch (Exception e){
            try{
                account = new CloudAccount(mnsProperties.getAccesskeyid(), mnsProperties.getAccesskeysecret(), mnsProperties.getAccountendpoint());
            }catch (Exception ex){
                throw new RuntimeException("aliyun mns account info not found.");
            }
        }
        MNSClient client = account.getMNSClient();

        try {
            CloudQueue queue = client.getQueueRef(name);

            SubscribableChannel subscribableChannel = (SubscribableChannel) outBoundChannel;
            subscribableChannel.subscribe(message -> {
                Object messageBody = message.getPayload();
                Message mnsMessage = new Message();
                try {
                    mnsMessage.setMessageBody(serialize(messageBody));
                    queue.putMessage(mnsMessage);
                } catch (IOException e) {
                    e.getMessage();
                }

            });
        } catch (ClientException var7) {
            var7.printStackTrace();
        } catch (ServiceException var8) {
            if (var8.getErrorCode().equals("QueueNotExist")) {
                System.out.println("Queue is not exist.Please create before use");
            } else if (var8.getErrorCode().equals("TimeExpired")) {
                System.out.println("The request is time expired. Please check your local machine timeclock");
            }
            var8.printStackTrace();
        } catch (Exception var9) {
            System.out.println("Unknown exception happened!");
            var9.printStackTrace();
        }
        return ()->{
            System.out.println("unbind channel.");
            client.close();
        };
    }

    public byte[] serialize(Object object) throws IOException {
        if(object instanceof byte[]){
            return (byte[])object;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(outputStream);
        dataOutputStream.writeObject(object);
        return outputStream.toByteArray();
    }
}
