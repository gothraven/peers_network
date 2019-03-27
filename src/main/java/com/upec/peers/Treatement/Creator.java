package com.upec.peers.Treatement;


import com.upec.peers.Server.SerializerBuffer;

public interface Creator<T> {
    T construct(SerializerBuffer serializerBuffer);
}
