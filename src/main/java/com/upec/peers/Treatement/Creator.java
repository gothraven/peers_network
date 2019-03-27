package com.upec.peers.Treatement;


import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

interface Creator<T> {
    T construct(ByteBuffer byteBuffer) throws BufferUnderflowException;
}
