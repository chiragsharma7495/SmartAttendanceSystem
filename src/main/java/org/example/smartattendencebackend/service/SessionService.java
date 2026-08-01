package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.CreateSessionRequest;
import org.example.smartattendencebackend.dto.request.UpdateSessionRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.SessionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SessionService {

    SessionResponse createSession(CreateSessionRequest request);

    SessionResponse getSessionById(Long id);

    PagedResponse<SessionResponse> getAllSessions(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    SessionResponse updateSession(Long id , UpdateSessionRequest request);

    void deleteSession(Long id);
}
