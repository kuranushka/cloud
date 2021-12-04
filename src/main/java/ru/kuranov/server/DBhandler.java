package ru.kuranov.server;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

@Slf4j
public class DBhandler {
    public DBhandler() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/cloud", "postgres", "root")) {
            if (conn == null) {
                log.debug("Connection to DB not established");
            } else {
                log.debug("Connectin to DB is Up...");
            }
        } catch (SQLException e) {
            System.err.format("SQL state: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
