package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.objects.SharedFile;
import com.upec.peers.network.nio.SerializerBuffer;

import java.util.ArrayList;
import java.util.List;

public class ListOfSharedFilesResponse implements Serializable {

    public static final byte ID = (byte)6;

    private List<SharedFile> listOfSharedFiles;

    public static final Creator<ListOfSharedFilesResponse> creator = serializerBuffer -> {
        int listLength = serializerBuffer.readInt();
        List<SharedFile> listOfFiles = new ArrayList<>();
        for (int i = 0; i < listLength; i++) {
            var name = serializerBuffer.readString();
            var size = serializerBuffer.readLong();
            listOfFiles.add(new SharedFile(name, size));
        }
        return new ListOfSharedFilesResponse(listOfFiles);
    };

    public ListOfSharedFilesResponse(List<SharedFile> listOfSharedFiles) {
        this.listOfSharedFiles = listOfSharedFiles;
    }

    @Override
    public SerializerBuffer serialize() {
        int bufferSize = 1 + 4;
        for (SharedFile listOfSharedFile : listOfSharedFiles)
            bufferSize = bufferSize + 4 + listOfSharedFile.getName().getBytes().length + 8;
        SerializerBuffer serializerBuffer = new SerializerBuffer(bufferSize);
        serializerBuffer.writeByte(ID);
        serializerBuffer.writeInt(listOfSharedFiles.size());
        for (SharedFile listOfSharedFile : listOfSharedFiles) {
            serializerBuffer.writeString(listOfSharedFile.getName());
            serializerBuffer.writeLong(listOfSharedFile.getSize());
        }
        return serializerBuffer;
    }

    @Override
    public String toString() {
        return listOfSharedFiles.toString();
    }
}
