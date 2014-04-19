package spinnytea.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import spinnytea.tools.net.Connection;
import spinnytea.tools.net.Server;

import java.io.IOException;

import org.junit.Test;

public class TestServer
{
	@Test
	public void startAndClose()
	throws IOException, InterruptedException
	{
		Server<TestConnection> server = null;
		try
		{
			server = new Server<>(5432, TestConnection.class);
			assertFalse(server.isRunning());

			new Thread(server).start();
			// give it until the count of 10 to start
			// if it doesn't then we can call it a failure
			for(int count = 10; !server.isRunning() && count > 0; count--)
				Thread.sleep(1000L);
			assertTrue(server.isRunning());

			server.close();
			assertFalse(server.isRunning());
		}
		finally
		{
			// try your darndest to shut it down
			if(server != null)
				server.close();
		}
	}

	private static class TestConnection
	extends Connection
	{
		@Override
		protected String getProtocol()
		{
			return "testconnection";
		}

		@Override
		protected boolean acceptProtocol(String protocol)
		{
			return getProtocol().equals(protocol);
		}

		@Override
		protected void nextObject(Object obj)
		{

		}
	}
}
