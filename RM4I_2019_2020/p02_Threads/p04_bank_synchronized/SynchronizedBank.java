package p04_bank_synchronized;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SynchronizedBank {
    private final int[] accounts;


    public SynchronizedBank(int accountsNum, int initialBalance) {
        this.accounts = new int[accountsNum];
        Arrays.fill(this.accounts, initialBalance);
    }


    // An alternative to locks is to use synchronized methods
    /* From official documentation:
        Making methods synchronized has two effects:
            - First, it is not possible for two invocations of synchronized methods on the _same
              object_ to interleave. When one thread is executing a synchronized method for an
              object, all other threads that invoke synchronized methods _for the same object_
              block (suspend execution) until the first thread is done with the object.
            - Second, when a synchronized method exits, it automatically establishes a happens-before
              relationship with any subsequent invocation of a synchronized method for the same object.
              This guarantees that changes to the state of the object are visible to all threads.
        Note that constructors cannot be synchronized — using the synchronized keyword with a constructor
        is a syntax error. Synchronizing constructors doesn't make sense, because only the thread that
        creates an object should have access to it while it is being constructed.
     */
    public synchronized void transfer(int from, int to, int amount) throws InterruptedException {

        // We want to wait here, as in the previous example. But now we do not have a condition object to wait for.
        // So what we do here, is use the wait() method defined in the Object class (which is the base
        // for all classes). wait() blocks until a signal is received (we will send it via notifyAll() method)
        while (this.accounts[from] < amount)
            this.wait();

        System.out.println(Thread.currentThread());
        this.accounts[from] -= amount;
        this.accounts[to] += amount;
        System.out.printf("Transfer from %3d to %3d: %5d\n", from, to, amount);
        System.out.println("Total balance: " + this.getTotalBalance());

        // We also use the Object's notifyAll() method to signal the threads that the transfer has been made
        this.notifyAll();
    }

    public int getTotalBalance() {
        return Arrays.stream(accounts).sum();
    }

    public int count() {
        return accounts.length;
    }
}
