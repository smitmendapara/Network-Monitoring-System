package service;

import util.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class ServerWebSocketEndPoint
{
    private static final Logger _logger = new Logger();

    private static Session session;

    @OnOpen
    public void onOpen(Session session)
    {
        try
        {
            ServerWebSocketEndPoint.session = session;
        }
        catch (Exception exception)
        {
            _logger.error("Server socket not open...", exception);
        }
    }

    @OnMessage
    public void onMessage(String message)
    {
        try
        {
            if (ServerWebSocketEndPoint.session.isOpen())
            {
                ServerWebSocketEndPoint.session.getBasicRemote().sendText(message);
            }
            else
            {
                ServerWebSocketEndPoint.session.getBasicRemote().sendText("Web Socket Session is not Open...");
            }
        }
        catch (Exception exception)
        {
            _logger.error("Message not sent successfully from server web socket...", exception);
        }
    }

    @OnClose
    public void onClose()
    {
        try
        {
            ServerWebSocketEndPoint.session.close();
        }
        catch (Exception exception)
        {
            _logger.error("Server socket not closed...", exception);
        }
    }

    @OnError
    public void onError(Throwable throwable)
    {
        try
        {
            StackTraceElement[] stackTrace = throwable.getStackTrace();

            for (int index = 0; index < stackTrace.length; index++)
            {
                System.out.println(stackTrace[index]);
            }
        }
        catch (Exception exception)
        {
            _logger.error("ServerWebSocket OnError method having error. ", exception);
        }
    }
}
