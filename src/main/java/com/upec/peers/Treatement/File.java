package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.io.*;
import java.nio.ByteBuffer;

public class File {

    private Long length;
    private String name;

    public File(Long l, String n) {
        this.length = l;
        this.name = n;
    }

    public Long getLength() {
        return this.length;
    }

    public String getName() {
        return this.name;
    }

    public ByteBuffer downLoad(long begin, long length) {
        SerializerBuffer byteBuffer = new SerializerBuffer(ByteBuffer.allocate(2048));
        int cmpt = 0;
        try {
// tester : taille + length < this.length
            BufferedReader reader = new BufferedReader(new FileReader(this.name));
            String line;
            while ((line = reader.readLine()) != null && cmpt < length) {
                byteBuffer.writeString(line);
                cmpt += line.length();
            }
            reader.close();
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", this.name);
            e.printStackTrace();
            return null;
        }
        return byteBuffer.getByteBuffer();
    }

    public void downloadFile(ByteBuffer bb, String path) throws IOException {


        SerializerBuffer byteBuffer = new SerializerBuffer(bb.flip());

        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        //while
        writer.write(byteBuffer.readString());

        writer.close();

    }

}
