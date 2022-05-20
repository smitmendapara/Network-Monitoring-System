package helper;

import util.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/serverEndPoint")
public class ServerWebSocket
{
    private static final Logger _logger = new Logger();

    private static Session session;

    @OnOpen
    public void onOpen(Session session)
    {
        try
        {
            ServerWebSocket.session = session;
        }
        catch (Exception exception)
        {
            _logger.error("Server socket not open.", exception);
        }
    }

    @OnMessage
    public void onMessage(String message)
    {
        try
        {
            if (ServerWebSocket.session.isOpen())
            {
                ServerWebSocket.session.getBasicRemote().sendText(message);
            }
            else
            {
                ServerWebSocket.session.getBasicRemote().sendText("Web Socket Session is not Open.");
            }
        }
        catch (Exception exception)
        {
            _logger.error("Message not sent successfully from server web socket.", exception);
        }
    }

    @OnClose
    public void onClose()
    {
        try
        {
            ServerWebSocket.session.close();
        }
        catch (Exception exception)
        {
            _logger.error("Server socket not closed!", exception);
        }
    }

    @OnError
    public void onError(Throwable throwable)
    {
        try
        {
            _logger.error("Server socket error occurred.", throwable);
        }
        catch (Exception exception)
        {
            _logger.error("ServerWebSocket OnError method having error.", exception);
        }
    }
}
