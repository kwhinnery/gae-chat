package com.twilio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;

public class MultichannelChatManager {
	private static HashSet<String> subs = new HashSet<String>();
	
	//Check subscription
	public static boolean isSubscribed(String sub) {
		return subs.contains(sub);
	}
	
	//Remove subscription
	public static void removeSub(String sub) {
		subs.remove(sub);
	}
	
	//Add a new subscription
	public static void addSub(String sub) {
		subs.add(sub);
	}

	// Send out a given message to all subscribers and clients
	public static void sendMessage(String body, String source) {
		Iterator<String> it = subs.iterator();
		while (it.hasNext()) {
			String sub = it.next();
			String messageBody = source + ": " + body;

			// We assume an at symbol is an XMPP client...
			if (sub.indexOf("@") >= 0) {
				JID jid = new JID(sub);
				Message msg = new MessageBuilder().withRecipientJids(jid).withBody(messageBody).build();
				XMPPService xmpp = XMPPServiceFactory.getXMPPService();
				xmpp.sendMessage(msg);
			}
			
			// If it starts with a "+" it's an SMS number...
			else if (sub.startsWith("+")) {
				TwilioRestClient client = new TwilioRestClient("ACCOUNT_SID", "AUTH_TOKEN");

				Map<String, String> params = new HashMap<String, String>();
				params.put("Body", messageBody);
				params.put("To", sub);
				params.put("From", "+16122948105");

				SmsFactory messageFactory = client.getAccount().getSmsFactory();

				try {
					Sms message = messageFactory.create(params);
					System.out.println(message.getSid());
				} catch (TwilioRestException e) {
					e.printStackTrace();
				}
			}

			// Otherwise, it's a browser-based client
			else {
				ChannelService channelService = ChannelServiceFactory.getChannelService();
				channelService.sendMessage(new ChannelMessage(sub,messageBody));
			}
		}
	}
}
