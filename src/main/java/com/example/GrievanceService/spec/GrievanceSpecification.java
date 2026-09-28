package com.example.GrievanceService.spec;

import org.springframework.data.jpa.domain.Specification;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GrievanceSpecification {

    public static Specification<Grievance> filter(
            GrievanceStatus status,
            Long categoryId,
            Long mandalId,
            List<Long> districtMandalIds,
            Long userId,
            List<Long> assignedGrievanceIds,
            LocalDateTime fromDate,
            LocalDateTime toDate) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (assignedGrievanceIds != null) {
                predicate = cb.and(predicate,
                        root.get("id").in(assignedGrievanceIds));
            }

            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            if (categoryId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("category").get("id"), categoryId));
            }

            if (mandalId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("mandalId"), mandalId));
            } else if (districtMandalIds != null && !districtMandalIds.isEmpty()) {
                predicate = cb.and(predicate,
                        root.get("mandalId").in(districtMandalIds));
            }

            if (userId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("userId"), userId));
            }

            if (fromDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }

            if (toDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            return predicate;
        };
    }
}