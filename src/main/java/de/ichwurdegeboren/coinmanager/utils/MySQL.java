package de.ichwurdegeboren.coinmanager.utils;

import de.ichwurdegeboren.coinmanager.main.Main;

import java.sql.*;

public class MySQL {

    private final Main plugin;

    private String HOST;
    private String PORT;
    private String DATABASE;
    private String USER;
    private String PASSWORD;

    private Connection con;

    public MySQL(String host, String port, String database, String user, String password, Main plugin) {
        System.out.println(host);
        System.out.println(port);
        System.out.println(database);
        System.out.println(user);
        System.out.println(password);
        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        this.plugin = plugin;

        connect();
    }

    private void connect() {
        try {
            this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
            plugin.getLogger().info("Die Verbindung zur MySQL wurde hergestellt!");
        } catch (SQLException e) {
            plugin.getLogger().info("Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
        }
    }

    public void close() {
        if (con == null) return;
        try {
            con.close();
            plugin.getLogger().info("Die Verbindung zur MySQL wurde Erfolgreich beendet!");
        } catch (SQLException e) {
            plugin.getLogger().info("Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
        }
    }

    public void update(String qry) {
        try {
            PreparedStatement ps = con.prepareStatement(qry);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(qry);
            ps.execute();
            ps.closeOnCompletion();
            rs = ps.getResultSet();
            return rs;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    public Connection getCon() {
        return con;
    }
}
