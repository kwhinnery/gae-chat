# Group Messaging for App Engine (Java)

This example application is a demo showing multichannel messaging/chat using the App Engine Channel API, App Engine's XMPP chat integration, and Twilio SMS.

## Running the application yourself

To run the app yourself, you will need to place your [Twilio account SID and auth token](https://www.twilio.com/user/account) inside the [Chat manager class](https://github.com/kwhinnery/gae-chat/blob/master/src/com/twilio/MultichannelChatManager.java#L56).

You will also need to [modify the Twilio number used to send outbound messages](https://github.com/kwhinnery/gae-chat/blob/master/src/com/twilio/MultichannelChatManager.java#L61) to [a number you control](https://www.twilio.com/user/account/phone-numbers/incoming).

## License

This example application is released under the MIT license.  See LICENSE for more info.
