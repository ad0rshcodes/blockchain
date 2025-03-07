package edu.grinnell.csc207.blockchain;

import java.util.Arrays;

/**
 * Hash class
 */
public class Hash {
    private byte[] data;

    /**
     * Constructs a Hash object with the given byte array.
     * 
     * @param data the byte array representing the hash value
     */
    public Hash(byte[] data) {
        this.data = data.clone(); // Defensive copy
    }

    /**
     * Returns a copy of the hash data.
     * 
     * @return a byte array containing the hash value
     */

    public byte[] getData() {
        return data.clone(); // Defensive copy
    }

    /**
     * Checks whether the hash is valid.
     * A valid hash is defined as having at least three bytes
     * and the first three bytes must be zero.
     * 
     * @return true if the hash meets the validity conditions, false otherwise
     */
    public boolean isValid() {
        if (data.length < 3) {
            return false;
        }
        return data[0] == 0 && data[1] == 0 && data[2] == 0;
    }

    /**
     * Returns a hexadecimal string representation of the hash.
     * 
     * @return the hash value as a hex string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", Byte.toUnsignedInt(b)));
        }
        return sb.toString();
    }

    /**
     * Checks whether this hash is equal to another object.
     * 
     * @param other the object to compare with
     * @return true if the other object is a Hash and contains the same data,
     *         false otherwise.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Hash)) {
            return false;
        }
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }
}