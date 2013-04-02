package com.twilio;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class XMPPReceiverServlet extends HttpServlet {
	// Handle Incoming XMPP Chat messages
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		XMPPService xmpp = XMPPServiceFactory.getXMPPService();
		Message msg = xmpp.parseMessage(req);

		JID fromJid = msg.getFromJid();
		String body = msg.getBody();

		// Unsubscribe, if requested
		if (body.startsWith("STOP")) {
			MultichannelChatManager.removeSub(fromJid.getId());
		} else {
			// If they aren't subscribed, subscribe them
			if (!MultichannelChatManager.isSubscribed(fromJid.getId())) {
				MultichannelChatManager.addSub(fromJid.getId());
			}
			MultichannelChatManager.sendMessage(body, "xmpp");
		}
	}
}
