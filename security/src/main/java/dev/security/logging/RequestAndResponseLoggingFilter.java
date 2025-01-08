package dev.security.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestAndResponseLoggingFilter.class);

    private static final String USER_NAME = "username";

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String requestBody = getRequestBody(request);
            String responseBody = getResponseBody(response);
            String clientIpAddress = request.getRemoteAddr();

            String message = String.format(
                    "\n🕒 Request processing time: %d ms 🕒%n" +
                            "-------------------------------------------%n" +
                            "Client IP: %s%n" +
                            "URI: %s%n" +
                            "Method: %s%n" +
                            "Status: %d%n" +
                            "Request Body: %s%n" +
                            "Response Body: %s%n" +
                            "-------------------------------------------",
                    duration,
                    clientIpAddress,
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus(),
                    requestBody,
                    responseBody
            );
            response.copyBodyToResponse();
            log.info(message);
        }
    }

    private static String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            if (isAuthRequest(request)) {
                content = parseAuthBody(content);
            }
            return logContent(content, request.getContentType(), request.getCharacterEncoding());
        } else {
            return "";
        }
    }

    private static String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String responseBody = logContent(content, response.getContentType(), response.getCharacterEncoding());
            if (isAuthResponse(response)) {
                responseBody = hideTokenInResponseBody(responseBody);
            }
            return responseBody;
        } else {
            return "";
        }
    }

    private static String logContent(byte[] content, String contentType, String contentEncoding) {
        try {
            MediaType mediaType = MediaType.valueOf(contentType);
            boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
            if (visible) {
                try {
                    Charset charset = Charset.forName(contentEncoding);
                    return new String(content, charset);
                } catch (UnsupportedCharsetException e) {
                    return "[" + content.length + " bytes content]";
                }
            } else {
                return " [" + content.length + "bytes content]";
            }
        } catch (Exception e) {
            return "can't read content (" + e.getMessage() + ")";
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    private static boolean isAuthRequest(ContentCachingRequestWrapper request) {
        return request.getRequestURI().contains("/api/auth") && request.getMethod().contains("POST");
    }

    private static byte[] parseAuthBody(byte[] authBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        String newBody;
        try {
            Map<String, String> userCredentials = objectMapper.readValue(new String(authBody), new TypeReference<HashMap<String, String>>() {
            });
            newBody = userCredentials.getOrDefault(USER_NAME, "");
            newBody = "{ " + USER_NAME + ":" + newBody + " }";
        } catch (Exception e) {
            newBody = "";
        }
        return newBody.getBytes();
    }

    private static boolean isAuthResponse(HttpServletResponse response) {
        return response.getStatus() == HttpStatus.OK.value() && response.getContentType().startsWith("application/json");
    }

    private static String hideTokenInResponseBody(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("token")) {
                ((ObjectNode) jsonNode).put("token", "[HIDDEN]");
            }
            return jsonNode.toString();
        } catch (JsonProcessingException e) {
            return responseBody;
        }
    }
}