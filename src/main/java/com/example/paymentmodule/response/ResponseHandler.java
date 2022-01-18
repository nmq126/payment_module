package com.example.paymentmodule.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHandler {
    private int status;
    private String message;
    private HashMap<String, Object> data;

    public static final class ResponseHandlerBuilder {
        private int status;
        private String message;
        private HashMap<String, Object> data;

        private ResponseHandlerBuilder() {
        }

        public static ResponseHandlerBuilder aResponseHandler() {
            return new ResponseHandlerBuilder();
        }

        public ResponseHandlerBuilder withStatus(int status) {
            this.status = status;
            return this;
        }

        public ResponseHandlerBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseHandlerBuilder withData(String key, Object value) {
            if (this.data == null) {
                this.data = new HashMap<>();
            }
            this.data.put(key, value);
            return this;
        }

        public ResponseHandler build() {
            ResponseHandler responseHandler = new ResponseHandler();
            responseHandler.setStatus(status);
            responseHandler.setMessage(message);
            responseHandler.setData(data);
            return responseHandler;
        }
    }
}