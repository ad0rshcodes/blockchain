package edu.grinnell.csc207.blockchain;

import java.security.NoSuchAlgorithmException;

/**
 * The BlockChain class manages a linked list of blocks, maintaining validity
 * and transaction integrity.
 */

public class BlockChain {
    /**
     * Represents a node in the blockchain, containing a block and a reference to
     * the next node.
     */
    private class Node {
        Block block;
        Node next;

        Node(Block block) {
            this.block = block;
            this.next = null;
        }
    }

    private Node first;
    private Node last;
    private int size;

    /**
     * Creates a blockchain with an initial transaction amount in the genesis block.
     * 
     * @param initial The initial transaction amount.
     * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
     */
    public BlockChain(int initial) throws NoSuchAlgorithmException {
        Block genesis = new Block(0, initial, null);
        first = new Node(genesis);
        last = first;
        size = 1;
    }

    /**
     * Mines a new block with the given transaction amount.
     * 
     * @param amount The transaction amount.
     * @return The newly mined block.
     * @throws NoSuchAlgorithmException If the hashing algorithm is not available.
     */
    public Block mine(int amount) throws NoSuchAlgorithmException {
        return new Block(size, amount, last.block.getHash());
    }

    /**
     * Gets the current size of the blockchain.
     * 
     * @return The number of blocks in the chain.
     */
    public int getSize() {
        return size;
    }

    /**
     * Appends a new block to the blockchain if it is valid.
     * 
     * @param blk The block to append.
     * @throws IllegalArgumentException If the block is invalid.
     */
    public void append(Block blk) throws IllegalArgumentException {
        if (!isValidAppend(blk)) {
            throw new IllegalArgumentException("Block is invalid for this chain");
        }
        Node newNode = new Node(blk);
        last.next = newNode;
        last = newNode;
        size++;
    }

    /**
     * Checks if a block is valid for appending to the chain.
     * 
     * @param blk The block to validate.
     * @return True if the block is valid, false otherwise.
     */
    private boolean isValidAppend(Block blk) {
        if (blk.getNum() != size) {
            return false;
        }
        if (!blk.getPrevHash().equals(last.block.getHash())) {
            return false;
        }
        if (!blk.getHash().isValid()) {
            return false;
        }
        return isValidTransaction(blk.getAmount());
    }

    /**
     * Checks if a new transaction amount maintains a non-negative balance.
     * 
     * @param newAmount The new transaction amount.
     * @return True if the transaction is valid, false otherwise.
     */
    private boolean isValidTransaction(int newAmount) {
        int aliceBalance = 0;
        Node current = first;
        while (current != null) {
            aliceBalance += current.block.getAmount();
            current = current.next;
        }
        aliceBalance += newAmount;
        return aliceBalance >= 0;
    }

    /**
     * Removes the last block from the blockchain.
     * 
     * @return True if the block was removed, false if only the genesis block
     *         remains.
     */
    public boolean removeLast() {
        if (size <= 1) {
            return false;
        }
        Node current = first;
        while (current.next != last) {
            current = current.next;
        }
        current.next = null;
        last = current;
        size--;
        return true;
    }

    /**
     * Gets the hash of the last block in the blockchain.
     * 
     * @return The hash of the last block.
     */
    public Hash getHash() {
        return last.block.getHash();
    }

    /**
     * Validates the entire blockchain, ensuring blocks are correctly linked and
     * transactions are valid.
     * 
     * @return True if the blockchain is valid, false otherwise.
     */
    public boolean isValidBlockChain() {
        Node current = first;
        int expectedNum = 0;
        Hash prevHash = null;
        int aliceBalance = 0;

        while (current != null) {
            Block blk = current.block;
            if (blk.getNum() != expectedNum || !blk.getHash().isValid()) {
                return false;
            }
            if (expectedNum > 0 && !blk.getPrevHash().equals(prevHash)) {
                return false;
            }
            aliceBalance += blk.getAmount();
            if (aliceBalance < 0) {
                return false;
            }
            prevHash = blk.getHash();
            expectedNum++;
            current = current.next;
        }
        return true;
    }

    /**
     * Prints the balances of Alice and Bob based on the blockchain's transaction
     * history.
     */

    public void printBalances() {
        int aliceBalance = 0;
        Node current = first;
        while (current != null) {
            aliceBalance += current.block.getAmount();
            current = current.next;
        }
        int bobBalance = first.block.getAmount() - aliceBalance;
        System.out.printf("Alice: %d, Bob: %d%n", aliceBalance, bobBalance);
    }

    /**
     * Returns a string representation of the blockchain.
     * 
     * @return A formatted string representing the blockchain.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.block.toString()).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}