package com.twilio;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.sdk.verbs.TwiMLResponse;

@SuppressWarnings("serial")
public class TwilioSmsServlet extends HttpServlet {
	// Handle Incoming SMS
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			TwiMLResponse twiml = new TwiMLResponse();

			// parse the body, looking for a command
			String smsBody = request.getParameter("Body");
			String smsFrom = request.getParameter("From");

			// Unsubscribe, if requested
			if (smsBody.startsWith("STOP")) {
				MultichannelChatManager.removeSub(smsFrom);
				com.twilio.sdk.verbs.Sms bye = new com.twilio.sdk.verbs.Sms("You have been unsubscribed.  Thank You!");
				twiml.append(bye);
			} else {
				// If they aren't subscribed, subscribe them
				if (!MultichannelChatManager.isSubscribed(smsFrom)) {
					MultichannelChatManager.addSub(smsFrom);
				}
				MultichannelChatManager.sendMessage(smsBody, "sms");
			}

			response.setContentType("application/xml");
			response.getWriter().print(twiml.toXML());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
}
