package org.example.smartattendencebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.CreateSubjectRequest;
import org.example.smartattendencebackend.dto.request.UpdateSubjectRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.SubjectResponse;
import org.example.smartattendencebackend.repository.SubjectRepository;
import org.example.smartattendencebackend.service.SubjectService;
//import org.hibernate.query.Page;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subject")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject( @Valid @RequestBody CreateSubjectRequest request){
         SubjectResponse response = subjectService.createSubject(request);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> GetSubjectByid(@PathVariable Long id){
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<SubjectResponse>> getAllSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {

        return ResponseEntity.ok(
                subjectService.getAllSubjects(
                        page,
                        size,
                        sortBy,
                        sortDirection
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubjectRequest request
    ) {

        return ResponseEntity.ok(
                subjectService.updateSubject(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(
            @PathVariable Long id
    ) {

        subjectService.DeleteSubject(id);

        return ResponseEntity.noContent().build();
    }
}
