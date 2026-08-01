package org.example.smartattendencebackend.specifications;

import org.example.smartattendencebackend.dto.request.AttendanceFilterRequest;
import org.example.smartattendencebackend.entity.Attendance;
import org.springframework.data.jpa.domain.Specification;

public final class AttendanceSpecifications {

    private AttendanceSpecifications(){

    }

    public static Specification<Attendance> buildSpecifications(AttendanceFilterRequest filter){

        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if(filter.getStudentId() != null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("student").get("id"),
                                filter.getStudentId()
                        )
                );
            }

            if(filter.getSubjectId() != null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("subject").get("id"),
                                filter.getSubjectId()
                        )
                );
            }

            if(filter.getTeacherId() != null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("teacher").get("id"),
                                filter.getTeacherId()
                        )
                );
            }

            if(filter.getStatus() != null){
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.between(
                                root.get("attendanceDate"),
                                filter.getStartDate(),
                                filter.getEndDate()
                        )
                );
            } else if (filter.getStartDate() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("attendanceDate"),
                                filter.getStartDate()
                        )
                );
            } else if (filter.getEndDate() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("attendanceDate"),
                                filter.getEndDate()
                        )
                );
            }

            return predicate;
        };
    }
}
