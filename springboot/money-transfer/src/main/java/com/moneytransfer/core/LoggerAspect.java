package com.moneytransfer.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @Around("within(com.moneytransfer.core.AbstractController+)")
    public Object logger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object value;
        LogModel logModel = new LogModel();
        Signature signature = proceedingJoinPoint.getSignature();

        logModel.setMethodName(signature.getName());
        logModel.setRequest(proceedingJoinPoint.getArgs());

        try {
            value = proceedingJoinPoint.proceed();
            if (value != null) {
                logModel.setResponse(value);
            }
            LOGGER.info("Success req/res is " + objectMapper.writeValueAsString(logModel));
        } catch (Throwable e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            writer.close();
            printWriter.close();
            logModel.setErrorTrace(writer.toString());
            LOGGER.error("Failure req/res is " + objectMapper.writeValueAsString(logModel));
            throw e;
        }

        return value;
    }
}

@Data
class LogModel {
    private String methodName;
    private Object request;
    private Object response;
    private String errorTrace;
}
