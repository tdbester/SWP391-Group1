package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Room;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RoomDAO {

    /**
     * Lấy danh sách phòng học khả dụng trong một slot cụ thể
     */
    public ArrayList<Room> getAvailableRoomsForSlot(LocalDate date, int slotId) {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = """
            SELECT r.Id, r.Code
            FROM Room r
            WHERE r.Id NOT IN (
                SELECT s.RoomId 
                FROM Schedule s 
                WHERE s.Date = ? AND s.SlotId = ?
            )
            ORDER BY r.Code
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ps.setInt(2, slotId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("Id"));
                room.setCode(rs.getString("Code"));
                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    /**
     * Lấy danh sách phòng học khả dụng cho một lịch học cụ thể (để thay đổi phòng)
     */
    public ArrayList<Room> getAvailableRoomsForSchedule(int scheduleId, LocalDate newDate) {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = """
            SELECT r.Id, r.Code
            FROM Room r
            WHERE r.Id NOT IN (
                SELECT s.RoomId 
                FROM Schedule s 
                WHERE s.Date = ? 
                AND s.SlotId = (SELECT SlotId FROM Schedule WHERE Id = ?)
                AND s.Id != ?
            )
            ORDER BY r.Code
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(newDate));
            ps.setInt(2, scheduleId);
            ps.setInt(3, scheduleId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("Id"));
                room.setCode(rs.getString("Code"));
                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    /**
     * Lấy thông tin phòng học theo ID
     */
    public Room getRoomById(int roomId) {
        String sql = "SELECT Id, Code FROM Room WHERE Id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("Id"));
                room.setCode(rs.getString("Code"));
                return room;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lấy tất cả phòng học
     */
    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = "SELECT Id, Code FROM Room ORDER BY Code";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("Id"));
                room.setCode(rs.getString("Code"));
                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }
}

