package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.ClassRooms;
import java.sql.*;

public class ClassRoomsDAO {
    public int insertClassRoomAndReturnId(ClassRooms classRoom) throws SQLException {
        String sql = "INSERT INTO ClassRooms (Name, CourseId, TeacherId, SlotId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, classRoom.getName());
            stmt.setInt(2, classRoom.getCourseId());
            stmt.setInt(3, classRoom.getTeacherId());
            stmt.setInt(4, classRoom.getSlotId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return -1;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                else return -1;
            }
        }
    }
}
