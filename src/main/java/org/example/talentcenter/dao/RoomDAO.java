package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    /**
     * Lấy tất cả phòng học
     */
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT Id, Code FROM Room ORDER BY Code";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("Id"));
                room.setCode(rs.getString("Code"));
                rooms.add(room);
            }
        }

        return rooms;
    }

    /**
     * Lấy thông tin phòng theo ID
     */
    public Room getRoomById(int roomId) throws SQLException {
        String sql = "SELECT Id, Code FROM Room WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setId(rs.getInt("Id"));
                    room.setCode(rs.getString("Code"));
                    return room;
                }
            }
        }

        return null;
    }
}