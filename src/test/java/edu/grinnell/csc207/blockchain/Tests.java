package edu.grinnell.csc207.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Tests {
    // TODO: fill me in with tests that you write for this project!

    @Test
    private static boolean testHashValid() {
        Hash validHash = new Hash(new byte[] { 0, 0, 0, 1, 2, 3 });
        boolean result = validHash.isValid();
        System.out.println("testHashValid: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testHashInvalid() {
        Hash invalidHash = new Hash(new byte[] { 1, 0, 0, 1, 2, 3 });
        boolean result = !invalidHash.isValid();
        System.out.println("testHashInvalid: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testHashToString() {
        Hash hash = new Hash(new byte[] { 0, (byte) 255, 16 });
        String expected = "00ff10";
        String actual = hash.toString();
        boolean result = expected.equals(actual);
        System.out.println("testHashToString: " + (result ? "PASS" : "FAIL") + " (Expected: " + expected + ", Got: "
                + actual + ")");
        return result;
    }

    @Test
    private static boolean testHashEqualsTrue() {
        Hash hash1 = new Hash(new byte[] { 0, 1, 2 });
        Hash hash2 = new Hash(new byte[] { 0, 1, 2 });
        boolean result = hash1.equals(hash2);
        System.out.println("testHashEqualsTrue: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testHashEqualsFalse() {
        Hash hash1 = new Hash(new byte[] { 0, 1, 2 });
        Hash hash2 = new Hash(new byte[] { 0, 1, 3 });
        boolean result = !hash1.equals(hash2);
        System.out.println("testHashEqualsFalse: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    // Block Tests
    private static boolean testBlockGenesis() throws NoSuchAlgorithmException {
        Block block = new Block(0, 100, null);
        boolean result = block.getNum() == 0 && block.getAmount() == 100 && block.getPrevHash() == null
                && block.getHash().isValid();
        System.out.println("testBlockGenesis: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testBlockWithPrevHash() throws NoSuchAlgorithmException {
        Hash prevHash = new Hash(new byte[] { 0, 0, 0, 1 });
        Block block = new Block(1, -50, prevHash);
        boolean result = block.getPrevHash().equals(prevHash) && block.getHash().isValid();
        System.out.println("testBlockWithPrevHash: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testBlockNonceConstructor() throws NoSuchAlgorithmException {
        Hash prevHash = new Hash(new byte[] { 0, 0, 0, 1 });
        Block mined = new Block(1, -50, prevHash);
        Block block = new Block(1, -50, prevHash, mined.getNonce());
        boolean result = block.getHash().equals(mined.getHash());
        System.out.println("testBlockNonceConstructor: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testBlockGetters() throws NoSuchAlgorithmException {
        Block block = new Block(2, 75, new Hash(new byte[] { 0, 0, 0, 1 }));
        boolean result = block.getNum() == 2 && block.getAmount() == 75 && block.getNonce() >= 0;
        System.out.println("testBlockGetters: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testBlockToString() throws NoSuchAlgorithmException {
        Block block = new Block(0, 100, null);
        String str = block.toString();
        boolean result = str.contains("Block 0") && str.contains("Amount: 100") && str.contains("prevHash: null");
        System.out.println("testBlockToString: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    // BlockChain Tests
    private static boolean testBlockChainInit() throws NoSuchAlgorithmException {
        BlockChain chain = new BlockChain(300);
        boolean result = chain.getSize() == 1 && chain.getHash().isValid();
        System.out.println("testBlockChainInit: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testBlockChainMine() throws NoSuchAlgorithmException {
        BlockChain chain = new BlockChain(300);
        Block mined = chain.mine(-150);
        boolean result = mined.getNum() == 1 && mined.getAmount() == -150 && mined.getHash().isValid();
        System.out.println("testBlockChainMine: " + (result ? "PASS" : "FAIL"));
        return result;
    }

    @Test
    private static boolean testBlockChainAppend() throws NoSuchAlgorithmException {
        BlockChain chain = new BlockChain(300);
        Block mined = chain.mine(-150);
        chain.append(mined);
        boolean result = chain.getSize() == 2 && chain.getHash().equals(mined.getHash());
        System.out.println("testBlockChainAppend: " + (result ? "PASS" : "FAIL"));
        return result;
    }
}
