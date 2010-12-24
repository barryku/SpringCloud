package com.barryku.gae.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.barryku.gae.model.TwilioCallback;

public class AmazonSnsService {

	private AmazonSNSClient sns;
	private String topicArn;
	
	public AmazonSnsService(AmazonSNSClient sns) {
		this.sns = sns;
	}
	
	public String sendTwilioMessage(TwilioCallback msg) {
		PublishRequest request = new PublishRequest(topicArn, msg.getBody(), msg.getFrom());
		PublishResult result = sns.publish(request);
		return result.getMessageId();
	}

	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}

	public String getTopicArn() {
		return topicArn;
	}

}
