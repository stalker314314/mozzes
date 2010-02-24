package org.mozzes.remoting.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines protocol of communication between remoting server and client.
 */
public class RemotingProtocol {

	private static final Logger logger = LoggerFactory.getLogger(RemotingProtocol.class);

	/** Communication streams */
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;

	private boolean compression;
	private boolean encryption;

	/**
	 * Creates new remoting protocol instance for server side use. Server will use compression and/or encryption only if
	 * that is requested by client.
	 * 
	 * @param socket Communication socket
	 * @throws IOException If there is some problem with communication over socket
	 */
	public static RemotingProtocol buildServerSide(Socket socket) throws IOException {
		return new RemotingProtocol(socket, true, false, false);
	}

	/**
	 * Creates new remoting protocol instance for client side use. There will be no compression or encryption
	 * 
	 * @param socket Communication socket
	 * @throws IOException If there is some problem with communication over socket
	 */
	public static RemotingProtocol buildClientSide(Socket socket) throws IOException {
		return new RemotingProtocol(socket, false, false, false);
	}

	/**
	 * Creates new remoting protocol instance for client side use
	 * 
	 * @param socket Communication socket
	 * @param compression Is compression enabled?
	 * @param encryption Is encryption enabled? THIS FEATURE IS NOT SUPPORTED YET, THERE IS NO ENCRYPTION
	 * @throws IOException If there is some problem with communication over socket
	 */
	public static RemotingProtocol buildClientSide(Socket socket, boolean compression, boolean encryption)
			throws IOException {
		return new RemotingProtocol(socket, false, compression, encryption);
	}

	/**
	 * Creates new remoting protocol instance
	 * 
	 * @param socket Communication socket
	 * @param serverSide Is this protocol instance for servers side?
	 * @param compression Is compression enabled?
	 * @param encryption Is encryption enabled? THIS FEATURE IS NOT SUPPORTED YET, THERE IS NO ENCRYPTION
	 * @throws IOException If there is some problem with communication over socket
	 */
	private RemotingProtocol(Socket socket, boolean serverSide, boolean compression, boolean encryption)
			throws IOException {
		createStreams(socket, serverSide);
		initCommunication(serverSide, compression, encryption);
		if (logger.isDebugEnabled()) {
			logger.debug((serverSide ? "Server" : "Client") + " side remoting protocol created (compression = "
				+ this.compression + ", encryption = " + this.encryption + ")");
		}
	}

	/**
	 * Sends object through socket according to protocol
	 * 
	 * @param object Object to send
	 * @throws IOException If there is some problem with communication over socket
	 */
	public synchronized void send(Object object) throws IOException {
		final byte[] preparedObject = prepareSend(object);
		dataOutputStream.writeInt(preparedObject.length);
		dataOutputStream.write(preparedObject);
		dataOutputStream.flush();
	}

	/**
	 * Receives object from socket according to protocol
	 */
	public synchronized Object receive() throws IOException, RemotingException {
		try {
			byte[] receivedData = new byte[dataInputStream.readInt()];
			int totalRead = 0;
			while (totalRead < receivedData.length) {
				int read = dataInputStream.read(receivedData, totalRead, receivedData.length - totalRead);
				if (read == -1)
					throw new RemotingException("Not enough data in stream");
				totalRead += read;
			}

			return prepareReceive(receivedData);
		} catch (ClassNotFoundException ex) {
			logger.error("Unknown class received from client", ex);
			throw new RemotingException(ex);
		} catch (DataFormatException ex) {
			logger.error("Unable to decompress data received from client", ex);
			throw new RemotingException(ex);
		}
	}

	/**
	 * Closes protocol and disables communication but doesn't close socket
	 */
	public void close() {
		if (dataInputStream != null) {
			try {
				dataInputStream.close();
			} catch (IOException ex) {
			}
			dataInputStream = null;
		}

		if (dataOutputStream != null) {
			try {
				dataOutputStream.close();
			} catch (IOException ex) {
			}
			dataOutputStream = null;
		}
	}

	private void createStreams(Socket socket, boolean serverSide) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("createStream() creating streams serverSide = " + serverSide);
		}
		if (serverSide) {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
		} else {
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
		}
		logger.debug("createStream() streams created"); 
	}

	private void initCommunication(boolean serverSide, boolean compression, boolean encryption) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("initCommunication() before - serverSide=" + serverSide);
		}
		if (serverSide) {
			this.compression = dataInputStream.readBoolean();
			this.encryption = dataInputStream.readBoolean();
		} else {
			this.compression = compression;
			this.encryption = encryption;
			dataOutputStream.writeBoolean(compression);
			dataOutputStream.writeBoolean(encryption);
			dataOutputStream.flush();
		}
		logger.debug("initCommunication() after");
	}

	private byte[] prepareSend(Object object) throws IOException {
		// serialize, compress, encrypt
		return encrypt(compress(serialize(object)));
	}

	private Object prepareReceive(byte[] response) throws IOException, ClassNotFoundException, DataFormatException {
		// decrypt, decompress, deserialize
		return deserialize(decompress(decrypt(response)));
	}

	private byte[] serialize(Object o) throws IOException {
		if (o == null)
			return null;

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		objStream.writeObject(o);
		objStream.flush();
		return byteStream.toByteArray();
	}

	private Object deserialize(byte[] b) throws IOException, ClassNotFoundException {
		if (b == null)
			return null;

		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(byteInputStream);
		return ois.readObject();
	}

	private byte[] compress(byte[] data) throws IOException {
		if (!compression)
			return data;

		// Create the compressor with requested level of compression
		Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_SPEED);
		compressor.setInput(data);
		compressor.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
		byte[] buf = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		bos.close();

		return bos.toByteArray();
	}

	private byte[] decompress(byte[] data) throws DataFormatException, IOException {
		if (!compression)
			return data;

		// Create the decompressor and give it the data to compress
		Inflater decompressor = new Inflater();
		decompressor.setInput(data);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			int count = decompressor.inflate(buf);
			bos.write(buf, 0, count);
		}
		bos.close();

		return bos.toByteArray();
	}

	private byte[] encrypt(byte[] data) {
		if (!encryption)
			return data;

		// TODO encryption
		return data;
	}

	private byte[] decrypt(byte[] data) {
		if (!encryption)
			return data;

		// TODO decryption
		return data;
	}
}
