package helper;

import util.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/serverEndPoint")
public class ServerWebSocket
{
    private static final Logger _logger = new Logger();

    private static Session session;

    private static Map<String, Session> map = new ConcurrentHashMap<>();

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
    public void handleMessage(Session session, String message)
    {
        try
        {
            map.put(message, session);
        }
        catch (Exception exception)
        {
            _logger.error("Message not sent successfully from server web socket.", exception);
        }
    }


    public void onMessage(String message, String userSession)
    {
        try
        {
            if (ServerWebSocket.session.isOpen())
            {
                map.get(userSession).getBasicRemote().sendText(message);
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
            _logger.error("Server socket not closed.", exception);
        }
    }

    @OnError
    public void onError(Throwable throwable)
    {
        try
        {
            _logger.error("Server socket error.", throwable);
        }
        catch (Exception exception)
        {
            _logger.error("ServerWebSocket OnError method having error.", exception);
        }
    }
}
