package spinnytea.tools.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is designed to send and receive java objects. Implementations are basically state machines that will handle objects as they become available. This
 * class enforces a protocol; it will send a protocol and wait to receive a protocol from the connection.
 * <p/>
 * Implementations must have a NoArgsConstructor
 * <p/>
 * Pro Tip: It is probably a good design to send a whole message/action as a single object.
 */
@Slf4j
@NoArgsConstructor
public abstract class Connection
implements Runnable
{
	/** the socket to read from for this client */
	@Getter
	private Socket mySocket;
	/** the server that this connection belongs to ~ if we remove this, then the server will need to periodically check to see if it's still alive */
	private Server<?> myServer;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	/** has the socket started to close itself? this just ensures we don't try to close it multiple times */
	private boolean running;

	/**
	 * @param server this can be null; if it is, then it will not be managed by anything; it will be a basic client connection
	 */
	protected final void start(Socket socket, Server<?> server)
	throws IOException
	{
		mySocket = socket;
		mySocket.setKeepAlive(true);
		ois = new ObjectInputStream(mySocket.getInputStream());
		oos = new ObjectOutputStream(mySocket.getOutputStream());
		running = true;

		if(server != null)
		{
			myServer = server;
			myServer.addConnection(this);
		}

		// start a thread to listen to the socket
		new Thread(this).start();
	}

	@Override
	public final void run()
	{
		try
		{
			// first, send the protocol that we will be using
			sendObject(getProtocol());

			try
			{
				// now, wait for them to send us the protocol they will be using
				if(!acceptProtocol((String) ois.readObject()))
				{
					log.warn("The protocol from this connection is not acceptable.");
					close();
				}
			}
			catch(ClassCastException | ClassNotFoundException | IOException e)
			{
				if(myServer == null || myServer.isRunning())
				{
					// should this be an error?
					log.warn("Invalid connection from (" + mySocket.getInetAddress() + "); I wonder what they wanted.", e);
					close();
				}
			}

			// now that everything is setup, continually read objects from the connection
			// when the connection has broken, or closed, then we will gracefully exit
			while(running && (myServer == null || myServer.isRunning()))
			{
				try
				{
					// read an object
					Object obj = ois.readObject();
					// pass it to the class to handle
					nextObject(obj);
				}
				catch(ClassNotFoundException | IOException e)
				{
					// the socket broke, so, lets shut it down
					if(myServer == null || myServer.isRunning())
					{
						log.warn("A socket (" + mySocket.getPort() + ") has terminated", e);
						close();
					}
				}
			}
		}
		finally
		{
			// just in case
			close();
		}
	}

	protected void sendObject(Object obj)
	{
		try
		{
			oos.writeObject(obj);
			oos.flush();
		}
		catch(IOException e)
		{
			// the socket broke, so, lets shut it down
			if(myServer == null || myServer.isRunning())
			{
				log.warn("A socket (" + mySocket.getPort() + ") has terminated", e);
				close();
			}
		}
	}

	/** when we are finished with the connect, then close it ~ this will call cleanup */
	protected void close()
	{
		if(running)
		{
			running = false;
			if(myServer != null)
				myServer.removeConnection(this);

			// clean up
			cleanup();

			try
			{
				oos.close();
			}
			catch(IOException e)
			{
				log.error("An I/O error occured when closing the socket OutputStream", e);
			}
			try
			{
				ois.close();
			}
			catch(IOException e)
			{
				log.error("An I/O error occured when closing the socket InputStream", e);
			}
			try
			{
				mySocket.close();
			}
			catch(IOException e)
			{
				log.warn("An I/O error occured when closing this socket", e);
			}
		}
	}

	/**
	 * This is the protocol that will be sent as soon as we start the connection.
	 * <p/>
	 * It is recommended that the protocol have a name and number; what is the protocol for, and what iteration is it on. (e.g. peer_v1 or server_v1)
	 */
	protected abstract String getProtocol();

	/**
	 * the first thing the connection should receive is a protocol. the protocol acts as a contract between producer and consumer. It let's the consumer know
	 * what kind of data to expect from the producer.
	 *
	 * @return true if the connection will be accepted; false if the connection should be closed
	 */
	protected abstract boolean acceptProtocol(String protocol);

	/** the connection is sending another object; here it is */
	protected abstract void nextObject(Object obj);

	/** the connection is going to close NOW, so take care of your last rights; this may be called at any time */
	protected abstract void cleanup();
}
