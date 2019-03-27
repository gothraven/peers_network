package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.BufferOverflowException;


interface Serializable {
    SerializerBuffer serialize() throws BufferOverflowException;
}
