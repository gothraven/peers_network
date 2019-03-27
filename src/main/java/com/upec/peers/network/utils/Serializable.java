package com.upec.peers.network.utils;

import com.upec.peers.network.nio.SerializerBuffer;


public interface Serializable {
    SerializerBuffer serialize();
}
