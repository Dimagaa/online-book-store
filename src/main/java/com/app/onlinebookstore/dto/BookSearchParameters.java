package com.app.onlinebookstore.dto;

public record BookSearchParameters(String title, String[] author, String[] isbn) {
}
