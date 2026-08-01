package org.example.smartattendencebackend.dto.request;

public interface StudentAttendanceCountProjection {

    long getTotalClasses();

    long getPresentClasses();

    long getAbsentClasses();
}
