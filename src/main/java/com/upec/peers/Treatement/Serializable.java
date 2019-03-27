package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.BufferOverflowException;


public interface Serializable {
    SerializerBuffer serialize();
}
