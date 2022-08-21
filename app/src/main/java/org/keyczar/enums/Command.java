package org.keyczar.enums;

import java.util.Objects;
import org.keyczar.i18n.Messages;

/* loaded from: classes3.dex */
public enum Command {
    CREATE("create"),
    ADDKEY("addkey"),
    PUBKEY("pubkey"),
    PROMOTE("promote"),
    DEMOTE("demote"),
    REVOKE("revoke"),
    USEKEY("usekey"),
    IMPORT_KEY("importkey"),
    EXPORT_KEY("exportkey");
    
    private final String name;

    Command(String str) {
        this.name = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    public static Command getCommand(String str) {
        Objects.requireNonNull(str);
        Command command = CREATE;
        if (str.equalsIgnoreCase(command.toString())) {
            return command;
        }
        Command command2 = ADDKEY;
        if (str.equalsIgnoreCase(command2.toString())) {
            return command2;
        }
        Command command3 = PUBKEY;
        if (str.equalsIgnoreCase(command3.toString())) {
            return command3;
        }
        Command command4 = PROMOTE;
        if (str.equalsIgnoreCase(command4.toString())) {
            return command4;
        }
        Command command5 = DEMOTE;
        if (str.equalsIgnoreCase(command5.toString())) {
            return command5;
        }
        Command command6 = REVOKE;
        if (str.equalsIgnoreCase(command6.toString())) {
            return command6;
        }
        Command command7 = USEKEY;
        if (str.equalsIgnoreCase(command7.toString())) {
            return command7;
        }
        Command command8 = IMPORT_KEY;
        if (str.equalsIgnoreCase(command8.toString())) {
            return command8;
        }
        Command command9 = EXPORT_KEY;
        if (!str.equalsIgnoreCase(command9.toString())) {
            throw new IllegalArgumentException(Messages.getString("Command.UnknownCommand", str));
        }
        return command9;
    }
}
