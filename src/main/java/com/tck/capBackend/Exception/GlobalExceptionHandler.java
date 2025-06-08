package com.tck.capBackend.Exception;

import com.tck.capBackend.utils.Utils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.sasl.AuthenticationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)  //When exception occur, this function takes precedence
@ControllerAdvice   //addresses exceptions across the entire app
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //1. When user send over data not readable
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable
    (HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        //return super.handleHttpMessageNotReadable(ex, headers, status, request);

        //customise
        MessageNotReadableException messageNotReadableException = new MessageNotReadableException();

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", messageNotReadableException.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //2. When user request for a resource (eg customer or transaction not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> HttpEntityNotFound(ResourceNotFoundException ex) {
        //Store response as HashMap
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error:", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //3. When user sends a RequestBody that is empty or incomplete
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((err)->{
                String field = ((FieldError) err).getField();
                String errMessage = err.getDefaultMessage();
                errors.put(field, errMessage);
        });

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //4. When user sends a password is blank
    @ExceptionHandler(PasswordBlankException.class)
    protected ResponseEntity<Object> handlePasswordBlankException(PasswordBlankException ex) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //5. When user access is not authorised - authentication failed
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AuthenticationException ex) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // 6. handle/trap errors produced by key constraints (e.g. adding an email that already exists)
    // @Table(uniqueConstraints = {@UniqueConstraint(name ="email", columnNames = "email")})
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        String cause = ex.getMostSpecificCause().getMessage();

        String constraintName = ((org.hibernate.exception.ConstraintViolationException)ex.getCause()).getConstraintName();
        constraintName = Utils.substringAtLastDelimiter(constraintName, ".");
        constraintName = Utils.capitalise(constraintName);

        if(ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException){
            if(cause.toLowerCase().contains("duplicate"))
                errorResponse.put("error", constraintName + " is already used.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        errorResponse.put("error", "Unknown Violation: " + cause);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    //7. For all exception not managed by GlobalExceptionHandler
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnspecifiedException(Exception ex) {

        UnspecifiedException unspecifiedException = new UnspecifiedException();
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
