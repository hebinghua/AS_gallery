package org.sqlite.database.sqlite;

/* loaded from: classes3.dex */
public class DatabaseObjectNotClosedException extends RuntimeException {
    public DatabaseObjectNotClosedException() {
        super("Application did not close the cursor or database object that was opened here");
    }
}
