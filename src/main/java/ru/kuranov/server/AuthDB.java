package ru.kuranov.server;

import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.msgtype.AuthMessage;

import java.sql.*;

@Slf4j
public class AuthDB extends DBConnections {
    public AuthMessage auth(AuthMessage msg) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/cloud", "postgres", "root")) {
            if (conn != null) {
                log.debug("Connection to DB is Up");
                String sql = "SELECT pass FROM data WHERE login=?;";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, msg.getUser());
                ResultSet resultSet = preparedStatement.executeQuery();
                String pass = "";
                while (resultSet.next()) {
                    pass = resultSet.getString("PASS");
                }
                if (msg.getPassword().equals(pass)) {
                    msg.setAuth(true);
                    log.debug("User {} accepted", msg);
                }
                conn.close();
                return msg;
            } else {
                log.debug("Connection to DB is not established");
            }
        } catch (SQLException e) {
            System.err.format("SQL state: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return msg;
    }
}