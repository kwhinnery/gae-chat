package com.twilio;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class ChatServlet extends HttpServlet {
	
	//Generate a client token for the GAE Channel API
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		
		//The token must be based on some unique identifier for the client - I am using the current time
		//for demo purposes...
		String clientId = String.valueOf(System.currentTimeMillis());
	    String token = channelService.createChannel(clientId);
	    
	    //Subscribe this client
	    MultichannelChatManager.addSub(clientId);
	    
	    //Reply with the token
		res.setContentType("text/plain");
		res.getWriter().print(token);
	}

	//Publish a chat message to all subscribers, if this subscriber exists
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		MultichannelChatManager.sendMessage(req.getParameter("message"), "web");
		res.setContentType("text/plain");
		res.getWriter().print("yep.");
	}
}
