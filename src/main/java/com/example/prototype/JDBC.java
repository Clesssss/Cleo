package com.example.prototype;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC {
    // Menggunakan java.util.logging.Logger
    // Sebagai pencatatan jika terjadi error atau semacamnya
    public static final Logger LOGGER = Logger.getLogger(JDBC.class.getName());

    // Menggunakan java.sql.Connection untuk class Connection
    // Sebagai penyimpanan koneksi dengan postgre database
    // Using Optional to prevent Null
    public Optional<Connection> connection = Optional.empty();

    public JDBC() {
        connection = getConnection();
    }

    public Optional<Connection> getConnection() {
        if (connection.isEmpty()) {
            // Jenis database yang digunakan
            String dbType = "jdbc:mysql:";
            // URL database yang dituju. localhost berarti sama dengan 127.0.0.1
            String dbUrl = "//localhost:";
            // Port yang digunakan untuk koneksi dengan database. DEFAULT-nya 5432
            // TERGANTUNG proses instalasi
            String dbPort = "3306/";
            // Nama Database yang digunakan
            String dbName = "cleo";
            // Nama User Database
            String dbUser = "root";
            // Password User
            String dbPass = "";

            try {
                connection = Optional.ofNullable(
                        // Menggunakan java.sql.DriverManager
                        // untuk mendapatkan koneksi
                        DriverManager.getConnection(
                                dbType + dbUrl + dbPort + dbName, dbUser, dbPass
                        )
                );
                if (connection != null) {
                    // Jika koneksi berhasil dibuat
                    System.out.println("Connection OK!");
                } else {
                    System.out.println("Connection Failed!");
                }
            } catch (SQLException ex) {
                // Jika di dalam try ada terjadi error
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }
}
