package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageRequestTest {

	private final String smallText = "Hello world";
	private final String longText = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Donec elementum ligula eu sapien consequat eleifend. Donec nec dolor erat, condimentum sagittis sem. Praesent porttitor porttitor risus, dapibus rutrum ipsum gravida et. Integer lectus nisi, facilisis sit amet eleifend nec, pharetra ut augue. Integer quam nunc, consequat nec egestas ac, volutpat ac nisi. Sed consectetur dignissim dignissim. Donec pretium est sit amet ipsum fringilla feugiat. Aliquam erat volutpat. Maecenas scelerisque, orci sit amet cursus tincidunt, libero nisl eleifend tortor, vitae cursus risus mauris vitae nisi. Cras laoreet ultrices ligula eget tempus. Aenean metus purus, iaculis ut imperdiet eget, sodales et massa. Duis pellentesque nisl vel massa dapibus non lacinia velit volutpat. Maecenas accumsan interdum sodales. In hac habitasse platea dictumst. Pellentesque ornare blandit orci, eget tristique risus convallis ut. Vivamus a sapien neque.";
	private final String emojiesText = "Hello world ∏”ﬁ@£! \uD83D\uDE0A \uD83D\uDE0B \uD83D\uDE0E \uD83D\uDE0D";

	@Test
	public void serailizeSmallMessage() {
		var message = new MessageRequest(smallText);
		SerializerBuffer sb = message.serialize();
		sb.getByteBuffer().flip();
		assertEquals(MessageRequest.ID, sb.readByte());
		assertEquals(smallText, sb.readString());
	}

	@Test
	public void serailizeLongMessage() {
		var message = new MessageRequest(longText);
		SerializerBuffer sb = message.serialize();
		sb.getByteBuffer().flip();
		assertEquals(MessageRequest.ID, sb.readByte());
		assertEquals(longText, sb.readString());
	}

	@Test
	public void serailizeMessageWithEmojies() {
		var message = new MessageRequest(emojiesText);
		SerializerBuffer sb = message.serialize();
		sb.getByteBuffer().flip();
		assertEquals(MessageRequest.ID, sb.readByte());
		assertEquals(emojiesText, sb.readString());
	}

	@Test
	public void deserializeSmallMessage() {
		var message = new MessageRequest(smallText);
		SerializerBuffer sb = message.serialize();
		sb.getByteBuffer().flip();

		assertEquals(MessageRequest.ID, sb.readByte());
		var messageObject = sb.readObject(MessageRequest.creator);
		assertNotNull(messageObject);
		assertEquals(smallText, messageObject.getMessage());
	}

	@Test
	public void deserializeLongMessage() {
		var message = new MessageRequest(longText);
		SerializerBuffer sb = message.serialize();
		sb.getByteBuffer().flip();

		assertEquals(MessageRequest.ID, sb.readByte());
		var messageObject = sb.readObject(MessageRequest.creator);
		assertNotNull(messageObject);
		assertEquals(longText, messageObject.getMessage());
	}

	@Test
	public void deserializeMessageWithEmojies() {
		var message = new MessageRequest(emojiesText);
		SerializerBuffer sb = message.serialize();
		sb.getByteBuffer().flip();

		assertEquals(MessageRequest.ID, sb.readByte());
		var messageObject = sb.readObject(MessageRequest.creator);
		assertNotNull(messageObject);
		assertEquals(emojiesText, messageObject.getMessage());
	}

	// todo add a test that it end an exeptetion when there is less bytes in the buffer
}