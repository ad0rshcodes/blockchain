package edu.grinnell.csc207.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * BlockChainDriver Class
 */
public class BlockChainDriver {

    /**
     * The main method runs the command-line blockchain interface.
     *
     * @param args Command-line arguments, where the first argument is the initial
     *             amount for the blockchain.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java BlockChainDriver <initial_amount>");
            return;
        }

        int initial;
        try {
            initial = Integer.parseInt(args[0]);
            if (initial < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Initial amount must be a non-negative integer");
            return;
        }

        try {
            BlockChain chain = new BlockChain(initial);
            Scanner scanner = new Scanner(System.in);
            Block candidate = null;

            while (true) {
                System.out.print(chain);
                System.out.print("Command? ");
                String command = scanner.nextLine().trim();

                switch (command) {
                    case "mine":
                        System.out.print("Amount transferred? ");
                        int amount = Integer.parseInt(scanner.nextLine());
                        candidate = chain.mine(amount);
                        System.out.printf("amount = %d, nonce = %d%n",
                                amount, candidate.getNonce());
                        break;

                    case "append":
                        if (candidate == null) {
                            System.out.println("No block mined yet!");
                            break;
                        }
                        System.out.print("Amount transferred? ");
                        int appendAmount = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nonce? ");
                        long nonce = Long.parseLong(scanner.nextLine());
                        Block toAppend = new Block(chain.getSize(),
                                appendAmount, chain.getHash(), nonce);
                        try {
                            chain.append(toAppend);
                            candidate = null; // Reset after successful append
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid block!");
                        }
                        break;

                    case "remove":
                        if (!chain.removeLast()) {
                            System.out.println("Cannot remove last block!");
                        }
                        break;

                    case "check":
                        System.out.println(chain.isValidBlockChain()
                                ? "Chain is valid!"
                                : "Chain is invalid!");
                        break;

                    case "report":
                        chain.printBalances();
                        break;

                    case "help":
                        printHelp();
                        break;

                    case "quit":
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid command! Type 'help' for options.");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hash algorithm not available: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Valid commands:");
        System.out.println("    mine: discovers the nonce for a given transaction");
        System.out.println("    append: appends a new block onto the end of the chain");
        System.out.println("    remove: removes the last block from the end of the chain");
        System.out.println("    check: checks that the block chain is valid");
        System.out.println("    report: reports the balances of Alice and Bob");
        System.out.println("    help: prints this list of commands");
        System.out.println("    quit: quits the program");
    }
}