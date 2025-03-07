package edu.grinnell.csc207.blockchain;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A single block of a blockchain.
 */
public class Block {
    private int num;
    private int amount;
    private Hash prevHash;
    private long nonce;
    private Hash hash;

    /**
     * Constructs a new block with a mined nonce.
     *
     * @param num      The block number.
     * @param amount   The transaction amount.
     * @param prevHash The hash of the previous block.
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available.
     */

    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = mineNonce();
        this.hash = computeHash();
    }

    /**
     * Constructs a block with a predefined nonce.
     *
     * @param num      The block number.
     * @param amount   The transaction amount.
     * @param prevHash The hash of the previous block.
     * @param nonce    The nonce value to use.
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available.
     */
    public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = computeHash();
    }

    /**
     * Mines a valid nonce by iterating until a valid hash is found.
     *
     * @return A valid nonce value.
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available.
     */
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

    /**
     * Computes the cryptographic hash of the block.
     *
     * @return The computed hash.
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available.
     */
    private Hash computeHash() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        updateDigest(md, nonce);
        return new Hash(md.digest());
    }

    /**
     * Updates the message digest with the block's data.
     *
     * @param md    The message digest to update.
     * @param nonce The nonce value.
     */
    private void updateDigest(MessageDigest md, long nonce) {
        md.update(ByteBuffer.allocate(4).putInt(num).array());
        md.update(ByteBuffer.allocate(4).putInt(amount).array());
        if (prevHash != null) {
            md.update(prevHash.getData());
        }
        md.update(ByteBuffer.allocate(8).putLong(nonce).array());
    }

    /**
     * Gets the block number.
     *
     * @return The block number.
     */
    public int getNum() {
        return num;
    }

    /**
     * Gets the transaction amount.
     *
     * @return The transaction amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the nonce value.
     *
     * @return The nonce value.
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * Gets the previous block's hash.
     *
     * @return The previous block's hash, or null if this is the first block.
     */
    public Hash getPrevHash() {
        return prevHash;
    }

    /**
     * Gets the current block's hash.
     *
     * @return The block's hash.
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * Returns a string representation of the block.
     *
     * @return A formatted string containing block details.
     */
    public String toString() {
        return String.format("Block %d (Amount: %d, Nonce: %d, prevHash: %s, hash: %s)",
                num, amount, nonce,
                prevHash == null ? "null" : prevHash.toString(), hash.toString());
    }
}