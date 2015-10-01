package com.udacity.gradle.utils;

/**
 * Created by Kush on 10/1/2015.
 */
public class SyncronizeTalker {
    public void doWait(long l){
        synchronized(this){
            try {
                this.wait(l);
            } catch(InterruptedException e) {
            }
        }
    }



    public void doNotify() {
        synchronized(this) {
            this.notify();
        }
    }


    public void doWait() {
        synchronized(this){
            try {
                this.wait();
            } catch(InterruptedException e) {
            }
        }
    }
}
