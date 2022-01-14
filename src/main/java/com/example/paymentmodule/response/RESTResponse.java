package com.example.paymentmodule.response;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Tạo dữ liệu trả về cho client qua api, đáp ứng một số yêu cầu từ mobile.
 * Không chứa http status code vì phải là tham số trong spring ResponseEntity.
 * */
public class RESTResponse {

    private HashMap<String, Object> response;

    // MUST be private.
    private RESTResponse() {
        this.response = new HashMap<>();
    }

    public HashMap<String, Object> getResponse() {
        return response;
    }

    public void setResponse(HashMap<String, Object> response) {
        this.response = response;
    }

    public void addResponse(String key, Object value) {
        this.response.put(key, value);
    }

    public static class Error {

        private HashMap<String, String> errors;
        private int status;
        private String message;

        public Error() {
            this.errors = new HashMap<>();
            this.status = 0;
            this.message = "";
        }

        public Error setStatus(int status) {
            this.status = status;
            return this;
        }

        public Error setMessage(String message) {
            this.message = message;
            return this;
        }

        public Error addError(String key, String value) {
            this.errors.put(key, value);
            return this;
        }

        public Error addErrors(HashMap<String, String> errors) {
            this.errors.putAll(errors);
            return this;
        }

        public HashMap<String, Object> build() {
            RESTResponse restResponse = new RESTResponse();
            restResponse.addResponse("status", this.status);
            restResponse.addResponse("message", this.message);
            String errorKey = "error";
            if (this.errors.size() > 1) {
                errorKey = "errors";
            }
            restResponse.addResponse(errorKey, this.errors);
            return restResponse.getResponse();
        }
    }

    public static class SimpleError {

        private int code;
        private String message;

        public SimpleError() {
            this.code = 400;
            this.message = "fail";
        }

        public SimpleError setCode(int code) {
            this.code = code;
            return this;
        }

        public SimpleError setMessage(String message) {
            this.message = message;
            return this;
        }

        public HashMap<String, Object> build() {
            RESTResponse restResponse = new RESTResponse();
            restResponse.addResponse("status", this.code);
            restResponse.addResponse("message", this.message);
            restResponse.addResponse("data", new ArrayList<>());
            return restResponse.getResponse();
        }
    }


    public static class Success {

        private int status;
        private String message;
        private List<Object> data;
        private RESTPagination pagination;

        public Success() {
            this.status = HttpStatus.OK.value();
            this.message = "Success";
            this.data = new ArrayList<>();
        }

        public Success setStatus(int status) {
            this.status = status;
            return this;
        }

        public Success setMessage(String message) {
            this.message = message;
            return this;
        }

        public Success setPagination(RESTPagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public Success addData(Object obj) {
            this.data.add(obj);
            return this;
        }

        public Success addData(List listObj) {
            this.data.addAll(listObj);
            return this;
        }

        public HashMap<String, Object> build() {
            RESTResponse restResponse = new RESTResponse();
            restResponse.addResponse("status", this.status);
            restResponse.addResponse("message", this.message);
            if (this.data.size() == 1) {
                restResponse.addResponse("data", this.data.get(0));

            } else {
                restResponse.addResponse("data", this.data);
            }
            if (this.pagination != null) {
                restResponse.addResponse("pagination", this.pagination);
            }
            return restResponse.getResponse();
        }

        public HashMap<String, Object> buildData() {
            RESTResponse restResponse = new RESTResponse();
            restResponse.addResponse("status", this.status);
            restResponse.addResponse("message", this.message);
            restResponse.addResponse("data", this.data);
            if (this.pagination != null) {
                restResponse.addResponse("pagination", this.pagination);
            }
            return restResponse.getResponse();
        }

//        System.out.println("Error example: " + new Gson().toJson(
//                new Error()
//                        .setStatus(HttpStatus.BAD_REQUEST.value())
//                        .setMessage("Everything is going to be !okie")
//                        .addError("title", "It required")
//                        .addError("content", "Required too")
//                        .build()));
//
//        System.out.println("Simple error example: " + new Gson().toJson(
//                new SimpleError()
//                        .setCode(1)
//                        .setMessage("Yolo oko")
//                        .build()));
//        System.out.println("Success response example: " + new Gson().toJson(
//                new Success()
//                        .setPagination(new RESTPagination(2, 10, 4))
//                        .addData(new Order())
//                        .addData(new Order())
//                        .setStatus(HttpStatus.OK.value())
//                        .build()));
    }
}
