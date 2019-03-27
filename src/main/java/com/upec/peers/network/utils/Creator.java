package com.upec.peers.network.utils;


import com.upec.peers.network.nio.SerializerBuffer;

public interface Creator<T> {
    T construct(SerializerBuffer serializerBuffer);
}
