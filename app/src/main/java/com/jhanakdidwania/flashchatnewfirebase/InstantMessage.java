package com.jhanakdidwania.flashchatnewfirebase;

public class InstantMessage {
    private String mMessage;
    private String mAuthor;

    public InstantMessage(String message, String author) {
        mMessage = message;
        mAuthor = author;
    }

    public InstantMessage(){
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getMessage() {
        return mMessage;
    }
}
