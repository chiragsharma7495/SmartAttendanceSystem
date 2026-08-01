package org.example.smartattendencebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.CreateSessionRequest;
import org.example.smartattendencebackend.dto.request.UpdateSessionRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.SessionResponse;
import org.example.smartattendencebackend.exception.SessionAlreadyExistException;
import org.example.smartattendencebackend.exception.SessionInUseException;
import org.example.smartattendencebackend.exception.SessionNotFoundException;
import org.example.smartattendencebackend.repository.SessionRepository;
import org.example.smartattendencebackend.entity.Session; // <-- ADD this line
import org.example.smartattendencebackend.util.PaginationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private static final Set<String> SESSION_SORT_FIELDS =
            Set.of("id", "name");

    @Override
    public SessionResponse createSession(CreateSessionRequest request) {

        if(sessionRepository.existsByStartYear(request.getStartYear())){
            throw new SessionAlreadyExistException("Session with start year " + request.getStartYear() + " already exists");
        }

        // create Entity
        Session session = new Session();
        session.setStartYear(request.getStartYear());
        session.setEndYear(request.getEndYear());
        session.setSessionName(request.getStartYear() + "-" + request.getEndYear());

        // save the session
        Session savesSession = sessionRepository.save(session);

        // map to Response
        SessionResponse response = new SessionResponse();
        response.setId(savesSession.getSessionId());
        response.setStartYear(savesSession.getStartYear());
        response.setEndYear(savesSession.getEndYear());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SessionResponse getSessionById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("Session Not Found With This ID: " + id));

        return mapToResponse(session);
    }

    @Override
    public PagedResponse<SessionResponse> getAllSessions(
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {

        Pageable pageable = PaginationUtils.createPageable(
                page,
                size,
                sortBy,
                sortDirection,
                SESSION_SORT_FIELDS
        );

        Page<SessionResponse> sessionPage =
                sessionRepository.findAll(pageable)
                        .map(this::mapToResponse);

        return PaginationUtils.toPagedResponse(sessionPage);
    }

    @Override
    @Transactional
    public SessionResponse updateSession(Long id, UpdateSessionRequest request) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("Session not found with this id : " + id));

        String updateName = request.getName().trim();

        if(sessionRepository.existsBySessionName(updateName)
            && !session.getSessionName().equalsIgnoreCase(updateName)){

            throw new SessionAlreadyExistException("Session already exists with name: " + updateName);
        }

        session.setSessionName(updateName);

        Session updatedSession = sessionRepository.save(session);

        return mapToResponse(updatedSession);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteSession(Long id) {

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("Session not found with this id : " +id));

        try{
            sessionRepository.delete(session);
            sessionRepository.flush();
        } catch(DataIntegrityViolationException exception){
            throw new SessionInUseException( "Session cannot be deleted because it is currently in use");
        }
    }

    private SessionResponse mapToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getSessionId());
        response.setStartYear(session.getStartYear());
        response.setEndYear(session.getEndYear());
        return response;
    }
}
