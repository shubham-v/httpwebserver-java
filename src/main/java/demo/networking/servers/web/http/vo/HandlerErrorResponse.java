package demo.networking.servers.web.http.vo;

import demo.networking.servers.web.http.utils.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandlerErrorResponse {

    private String requestId;
    private Date timestamp;
    private String message;
    private String errorType;
    private String errorMessage;
    private String errorCode;
    private String errorCause;
    private Map<String, Object> errorMessages;
    private Map<String, Object> messages;
    private String responseId;

    @Override
    public String toString() {
        return JSONUtils.get(this);
    }

}
