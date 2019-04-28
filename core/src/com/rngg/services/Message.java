package com.rngg.services;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Message {
    private byte[] data;
    private ByteBuffer buffer;
    private String senderID;
    private int describeContents;



    public Message(byte[] data, String senderID, int describeContents){
        this.data = data;
        buffer = ByteBuffer.wrap(data);
        this.senderID = senderID;
        this.describeContents = describeContents;
    }

    public Message copy(){
        byte[] c = new byte[data.length - buffer.position()];
        System.arraycopy(data, buffer.position(), c,0, c.length);
        return new Message(c, this.senderID, this.describeContents);
    }

    public ByteBuffer getBuffer(){
        return buffer;
    }


    public Vector2 getVector2(Vector2 dst){
        dst.set(buffer.getFloat(), buffer.getFloat());
        return dst;
    }

    public Vector2 getVector2(){
        return getVector2(new Vector2());
    }

    public void putVector(Vector2 src){
        buffer.putFloat(src.x);
        buffer.putFloat(src.y);
    }

    public void putVector(Vector3 src){
        buffer.putFloat(src.x);
        buffer.putFloat(src.y);
        buffer.putFloat(src.z);
    }

    public Vector3 getVector3(Vector3 dst){
        dst.set(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        return dst;
    }

    public Vector3 getVector3(){
        return getVector3(new Vector3());
    }

    public String getString(){
        int len = buffer.getInt();
        byte[] chars = new byte[len];
        buffer.get(chars);
        return new String(chars, StandardCharsets.UTF_8);
    }

    public int getInt(){
        return buffer.getInt();
    }

    public void putInt(int number){
        buffer.putInt(number);
    }

    public void putString(String str){
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        buffer.putInt(data.length);
        buffer.put(data);
    }

    public String getSender(){
        return senderID;
    }

    public byte[] getData(){
        return data;
    }

    public byte[] getCompact(){
        return Arrays.copyOfRange(data, 0, buffer.position());
    }
}
