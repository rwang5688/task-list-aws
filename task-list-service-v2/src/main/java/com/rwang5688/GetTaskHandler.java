package com.rwang5688;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rwang5688.dal.Task;


public class GetTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = LoggerFactory.getLogger(GetTaskHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        logger.info("received: {}", input);
        try {
            // get the 'pathParameters' from input
            Map<String, String> pathParameters =  (Map<String, String>)input.get("pathParameters");
            String user_id = pathParameters.get("user_id");
            String task_id = pathParameters.get("task_id");

            // get the Task by id
            Task task = new Task().get(user_id, task_id);

            // send the response back
            if (task != null) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(task)
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Task with user_id=" + user_id + " task_id:=" + task_id + " not found.")
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in retrieving task: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in retrieving task: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }

}

