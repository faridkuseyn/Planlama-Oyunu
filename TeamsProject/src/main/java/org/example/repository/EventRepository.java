package org.example.repository;

import org.example.model.Event;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventRepository {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/teamsproject";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    public boolean addEvent(Event event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            // Veritabanı bağlantısını aç
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Sorguyu hazırla
            String query = "INSERT INTO events (processingTime, startTime, endTime, eventType, eventDescription) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, event.getProcessingTime().toString());
            stmt.setString(2, event.getStartTime());
            stmt.setString(3, event.getEndTime());
            stmt.setString(4, event.getEventType());
            stmt.setString(5, event.getEventDescription());

            // Sorguyu çalıştır ve etkilenen satır sayısını kontrol et
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları serbest bırak
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public boolean deleteEvent(String eventDescription, LocalDate processingTime, String startTime, String endTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            // Veritabanı bağlantısını aç
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Sorguyu hazırla
            String query = "DELETE FROM events WHERE eventDescription = ? and processingTime = ? and startTime = ? and endTime = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, eventDescription);
            stmt.setString(2, processingTime.toString());
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);


            // Sorguyu çalıştır ve etkilenen satır sayısını kontrol et
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları serbest bırak
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public boolean updateEvent(Event event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            // Veritabanı bağlantısını aç
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Sorguyu hazırla
            String query = "UPDATE events SET processingTime = ?, startTime = ?, endTime = ?, eventType = ?, eventDescription = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, event.getProcessingTime().toString());
            stmt.setString(2, event.getStartTime());
            stmt.setString(3, event.getEndTime());
            stmt.setString(4, event.getEventType());
            stmt.setString(5, event.getEventDescription());
            stmt.setInt(6, event.getId());

            // Sorguyu çalıştır ve etkilenen satır sayısını kontrol et
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları serbest bırak
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public java.util.List<Event> getEventsByDate(Date date) {
        java.util.List<Event> events = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT * FROM teamsproject.events WHERE processingTime = ?";
            stmt = conn.prepareStatement(query);
            stmt.setDate(1, date);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                Date processingTime = rs.getDate("processingTime");
                String eventType = rs.getString("eventType");
                String eventDescription = rs.getString("eventDescription");

                Event event = new Event(processingTime.toLocalDate(),startTime,endTime,eventType,eventDescription);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return events;
    }

    public java.util.List<Event> getAllEvents() {
        java.util.List<Event> events = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();
            String query = "SELECT * FROM teamsproject.events";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                Date processingTime = rs.getDate("processingTime");
                String eventType = rs.getString("eventType");
                String eventDescription = rs.getString("eventDescription");

                Event event = new Event(processingTime.toLocalDate(),startTime,endTime,eventType,eventDescription);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return events;
    }


}