package com.pxw.smartchat.config.exception.librarian;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LibrarianException extends RuntimeException {

    public LibrarianException(String message) {
        super(message);
    }

    public LibrarianException(Throwable cause) {
        super(cause);
    }

    public LibrarianException(String message, Throwable cause) {
        super(message, cause);
    }
}
