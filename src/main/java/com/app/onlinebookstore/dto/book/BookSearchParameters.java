package com.app.onlinebookstore.dto.book;

public record BookSearchParameters(String title, String[] author, String[] isbn) {
}
