package org.example.talentcenter.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class ClassRooms {
    private int id;
    private String name;
    private int courseId;
    private int teacherId;
    private int slotId;
    private String roomCode;

    // Thông tin bổ sung cho hiển thị
    private String courseTitle;
    private String teacherName;
    private LocalTime startTime;
    private LocalTime endTime;
    private int studentCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int roomId;

    // Date fields for JSP compatibility
    private Date startTimeAsDate;
    private Date endTimeAsDate;
    private Date startDateAsDate;
    private Date endDateAsDate;
    private Date endDateAsUtilDate;

    public ClassRooms() {
    }

    public ClassRooms(int id, int teacherId, int courseId, String name, int slotId) {
        this.id = id;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.name = name;
        this.slotId = slotId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Date fields for JSP compatibility
    public Date getStartTimeAsDate() {
        return startTimeAsDate;
    }

    public void setStartTimeAsDate(Date startTimeAsDate) {
        this.startTimeAsDate = startTimeAsDate;
    }

    public Date getEndTimeAsDate() {
        return endTimeAsDate;
    }

    public void setEndTimeAsDate(Date endTimeAsDate) {
        this.endTimeAsDate = endTimeAsDate;
    }

    // Add getters and setters for date compatibility
    public Date getStartDateAsDate() {
        return startDateAsDate;
    }

    public void setStartDateAsDate(Date startDateAsDate) {
        this.startDateAsDate = startDateAsDate;
    }

    public Date getEndDateAsDate() {
        return endDateAsDate;
    }

    public void setEndDateAsDate(Date endDateAsDate) {
        this.endDateAsDate = endDateAsDate;
    }
    public Date getEndDateAsUtilDate() {
        return endDateAsUtilDate;
    }
    public void setEndDateAsUtilDate(Date endDateAsUtilDate) {
        this.endDateAsUtilDate = endDateAsUtilDate;
    }
    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public String getRoomCode() {
        return roomCode;
    }
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}