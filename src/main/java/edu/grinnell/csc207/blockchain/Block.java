package edu.grinnell.csc207.blockchain;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    private int num;
    private int amount;
    private Hash prevHash;
    private long nonce;
    private Hash hash;

    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = mineNonce();
        this.hash = computeHash();
    }

    public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = computeHash();
    }

    private long mineNonce() throws NoSuchAlgorithmException {
        long candidate = 0;
        while (true) {
            MessageDigest md = MessageDigest.getInstance("sha-256");
            updateDigest(md, candidate);
            Hash candidateHash = new Hash(md.digest());
            if (candidateHash.isValid()) {
                return candidate;
            }
            candidate++;
        }
    }

    private Hash computeHash() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        updateDigest(md, nonce);
        return new Hash(md.digest());
    }

    private void updateDigest(MessageDigest md, long nonce) {
        md.update(ByteBuffer.allocate(4).putInt(num).array());
        md.update(ByteBuffer.allocate(4).putInt(amount).array());
        if (prevHash != null) {
            md.update(prevHash.getData());
        }
        md.update(ByteBuffer.allocate(8).putLong(nonce).array());
    }

    public int getNum() {
        return num;
    }

    public int getAmount() {
        return amount;
    }

    public long getNonce() {
        return nonce;
    }

    public Hash getPrevHash() {
        return prevHash;
    }

    public Hash getHash() {
        return hash;
    }

    public String toString() {
        return String.format("Block %d (Amount: %d, Nonce: %d, prevHash: %s, hash: %s)",
                num, amount, nonce, prevHash == null ? "null" : prevHash.toString(), hash.toString());
    }
}