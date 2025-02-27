package com.example.notes.database

enum class NotesColumn(public val column: String) {
    TITLE("title"),
    CONTENT("content"),
    COLOR("color"),
    TIMESTAMP("timestamp"),
    REMIND_AT("remind_at"),
    REMIND_FREQ("reminder_frequency")
}

enum class ReminderFrequency {
    HOURLY, DAILY, WEEKLY, BIWEEKLY, MONTHLY
}

enum class SortDirection(public val direction: String) {
    ASCENDING("ASC"),
    DESCENDING("DESC")
}