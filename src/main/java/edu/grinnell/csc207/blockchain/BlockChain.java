package edu.grinnell.csc207.blockchain;

import java.security.NoSuchAlgorithmException;

public class BlockChain {
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

    public BlockChain(int initial) throws NoSuchAlgorithmException {
        Block genesis = new Block(0, initial, null);
        first = new Node(genesis);
        last = first;
        size = 1;
    }

    public Block mine(int amount) throws NoSuchAlgorithmException {
        return new Block(size, amount, last.block.getHash());
    }

    public int getSize() {
        return size;
    }

    public void append(Block blk) throws IllegalArgumentException {
        if (!isValidAppend(blk)) {
            throw new IllegalArgumentException("Block is invalid for this chain");
        }
        Node newNode = new Node(blk);
        last.next = newNode;
        last = newNode;
        size++;
    }

    private boolean isValidAppend(Block blk) {
        if (blk.getNum() != size)
            return false;
        if (!blk.getPrevHash().equals(last.block.getHash()))
            return false;
        if (!blk.getHash().isValid())
            return false;
        return isValidTransaction(blk.getAmount());
    }

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

    public boolean removeLast() {
        if (size <= 1)
            return false;
        Node current = first;
        while (current.next != last) {
            current = current.next;
        }
        current.next = null;
        last = current;
        size--;
        return true;
    }

    public Hash getHash() {
        return last.block.getHash();
    }

    public boolean isValidBlockChain() {
        Node current = first;
        int expectedNum = 0;
        Hash prevHash = null;
        int aliceBalance = 0;

        while (current != null) {
            Block blk = current.block;
            if (blk.getNum() != expectedNum || !blk.getHash().isValid())
                return false;
            if (expectedNum > 0 && !blk.getPrevHash().equals(prevHash))
                return false;
            aliceBalance += blk.getAmount();
            if (aliceBalance < 0)
                return false;
            prevHash = blk.getHash();
            expectedNum++;
            current = current.next;
        }
        return true;
    }

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