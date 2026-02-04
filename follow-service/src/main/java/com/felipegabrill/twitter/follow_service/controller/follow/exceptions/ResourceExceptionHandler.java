package com.felipegabrill.twitter.follow_service.controller.follow.exceptions;

import com.felipegabrill.twitter.follow_service.service.follow.exceptions.AlreadyFollowingException;
import com.felipegabrill.twitter.follow_service.service.follow.exceptions.NotFollowingException;
import com.felipegabrill.twitter.follow_service.service.follow.exceptions.UserCannotFollowSelfException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler  {

    @ExceptionHandler(UserCannotFollowSelfException.class)
    public ResponseEntity<StandardError> userCannotFollowSelf(UserCannotFollowSelfException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("User cannot follow themselves");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<StandardError> alreadyFollowing(AlreadyFollowingException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("User is already following the target");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NotFollowingException.class)
    public ResponseEntity<StandardError> notFollowing(NotFollowingException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("User is not following the target");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
