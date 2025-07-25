package org.example.talentcenter.dao;

import org.example.talentcenter.config.DBConnect;
import org.example.talentcenter.model.Slot;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class SlotDAO {
    public List<Slot> getAll() throws SQLException {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT Id, StartTime, EndTime FROM Slot ORDER BY StartTime";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Slot slot = new Slot();
                slot.setId(rs.getInt("Id"));
                slot.setStartTime(rs.getTime("StartTime").toLocalTime());
                slot.setEndTime(rs.getTime("EndTime").toLocalTime());
                slots.add(slot);
            }
        }
        return slots;
    }
}