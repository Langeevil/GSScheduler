package br.com.gabrielsiqueira.GSScheduler.exceptions;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class CustomExceptionResponse {
    private Date timeStamp;
    private String message;
    private String details;

    // Getters
    public Date getTimeStamp() { return timeStamp; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }

    // Setters
    public void setTimeStamp(Date timeStamp) { this.timeStamp = timeStamp; }
    public void setMessage(String message) { this.message = message; }
    public void setDetails(String details) { this.details = details; }
}
