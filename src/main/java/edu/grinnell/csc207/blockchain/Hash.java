package edu.grinnell.csc207.blockchain;

import java.util.Arrays;

public class Hash {
    private byte[] data;

    public Hash(byte[] data) {
        this.data = data.clone(); // Defensive copy
    }

    public byte[] getData() {
        return data.clone(); // Defensive copy
    }

    public boolean isValid() {
        if (data.length < 3)
            return false;
        return data[0] == 0 && data[1] == 0 && data[2] == 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", Byte.toUnsignedInt(b)));
        }
        return sb.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Hash))
            return false;
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }
}