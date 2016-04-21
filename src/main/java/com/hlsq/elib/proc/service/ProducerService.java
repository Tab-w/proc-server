package com.hlsq.elib.proc.service;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
	@Autowired
	private JmsTemplate queueJmsTemplate;

	public void sendObjectMessage(Destination destination, final Map<String, Object> map) {
		queueJmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createObjectMessage((Serializable) map);
				return message;
			}
		});
	}

	public void sendStringMessage(Destination destination, final String message) {
		queueJmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}

	@SuppressWarnings({ "unchecked" })
	public Map<String, Object> receiveMessage(Destination destination) {
		ObjectMessage message = (ObjectMessage) queueJmsTemplate.receive(destination);
		Map<String, Object> map = null;
		if (message != null) {
			try {
				map = (Map<String, Object>) message.getObject();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
