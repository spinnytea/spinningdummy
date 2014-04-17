package spinnytea.tools.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is a server in that it needs to listen on a port for another connection. This is used for both a "client-server" model as well as a "peer-to-peer"
 * model.
 */
@Slf4j
public class Server<T extends Connection>
implements Runnable
{
	/** the class that will do the consuming */
	private final Class<T> consumerClazz;

	/** the socket to listen on */
	private final ServerSocket serverSocket;

	/** when we are supposed to stop, then this will be set to false; this will signal to the connections to disconnect */
	@Getter
	// XXX can we remove the getter
	private boolean running;

	/** a list to store the current connects; this way they can be shut down */
	private final Collection<Connection> connections;

	public Server(int port, Class<T> consumerClazz)
	throws IOException
	{
		this.consumerClazz = consumerClazz;
		serverSocket = new ServerSocket(port);
		connections = new LinkedList<Connection>();
	}

	@Synchronized("connections")
	public final void addConnection(Connection conn)
	{
		connections.add(conn);
		log.warn("New connection!" + //
		"\n\tFrom: " + conn.getMySocket().getInetAddress() + ":" + conn.getMySocket().getPort() + //
		"\n\tTo:   " + conn.getMySocket().getLocalSocketAddress());
		log.debug("There are now " + connections.size() + " connections");
	}

	@Synchronized("connections")
	public final void removeConnection(Connection conn)
	{
		connections.remove(conn);
		log.info("Closing connection!" + //
		"\n\tFrom: " + conn.getMySocket().getInetAddress() + ":" + conn.getMySocket().getPort() + //
		"\n\tTo:   " + conn.getMySocket().getLocalSocketAddress());
		log.debug("There are now " + connections.size() + " connections");
	}

	@Override
	public final void run()
	{
		log.info("Starting server");

		try
		{
			while(running)
			{
				log.debug("Waiting for a connection");
				try
				{
					Socket socket = serverSocket.accept();
					Connection conn = null;

					try
					{
						conn = consumerClazz.newInstance();
						conn.start(socket, this);
					}
					catch(Exception e)
					{
						log.error("An error occured while creating a ServerConsumer", e);
						if(conn != null)
							conn.close();
					}
				}
				catch(IOException e)
				{
					if(running)
						log.error("An I/O error occurs when waiting for a connection", e);
					// don't stop the server from this
				}
			} // end while(running)
		}
		catch(Exception e)
		{
			log.error("Unknown exception", e);
		}
		finally
		{
			// just in case
			if(running)
				close();
		}

		log.info("End of run method reached for the Server");
	}

	public final void close()
	{
		log.info("Shutting down the server.");

		// tell everything that it's time to stop
		running = false;

		// shutdown the connections manually
		while(!connections.isEmpty())
			connections.iterator().next().close();

		// now that everything is closed, shut down the server
		// this will cause the .accept() method to throw an exception and stop
		try
		{
			serverSocket.close();
		}
		catch(Exception e)
		{
			log.warn("Error while shutting down server.", e);
		}
	}
}
