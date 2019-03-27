package com.upec.peers.Treatement;


import com.upec.peers.Server.SerializerBuffer;

import java.nio.BufferUnderflowException;

interface Creator<T> {
    T construct(SerializerBuffer serializerBuffer) throws BufferUnderflowException;
}
