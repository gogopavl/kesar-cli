package com.pvlrs.kesar.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsoleMessages {
    // Colours
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String KESAR_CLI_BANNER = ANSI_CYAN + "\n\n" +
            "   _     ____  __    __    ___       __   _     _  \n" +
            "  | |_/ | |_  ( (`  / /\\  | |_)     / /` | |   | |  \n" +
            "  |_| \\ |_|__ _)_) /_/--\\ |_| \\     \\_\\_ |_|__ |_|  \n\n" + ANSI_RESET;

    // Messages
    public static final String PASSWORD_PROMPT = "Enter value for --password (Password used to encrypt data with): ";
}
