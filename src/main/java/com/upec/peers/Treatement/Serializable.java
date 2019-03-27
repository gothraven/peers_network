package com.upec.peers.Treatement;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;




interface Serializable {
    ByteBuffer serialize() throws BufferOverflowException;
}
