package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionFactory {
    private static Connection connection;
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException exception) {
            System.out.println("Erro: " + exception.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            if (isBlank(url) || isBlank(user) || isBlank(password)) {
                throw new IllegalStateException(
                        "Configure DB_URL, DB_USER e DB_PASSWORD no arquivo .env na raiz do projeto.");
            }

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException exception) {
            System.out.println("Erro de SQL: " + exception.getMessage());
        } catch (ClassNotFoundException exception) {
            System.out.println("Erro nome da classe: " + exception.getMessage());
        }

        return connection;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
