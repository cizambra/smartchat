package com.pxw.smartchat.model.librarian;

import lombok.NonNull;

public class LibrarianFactory {
    public enum LibrarianType {
        PLAIN
    }

    public Librarian getLibrarian(final @NonNull String librarianType) {
        if (librarianType.equalsIgnoreCase(LibrarianType.PLAIN.name())) {
            return new PlainLibrarian();
        } else {
            throw new IllegalArgumentException(String.format("Invalid librarian type: %s", librarianType));
        }
    }
}
