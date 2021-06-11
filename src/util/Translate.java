package util;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map; // 네이버 기계번역 (Papago SMT) API 예제

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Translate {
    public static void main(String[] args) throws ParseException {
        Translate.translate("준수는 짱짱이다");
    }

    public static String translate(String pMessage) throws ParseException {
        /* 애플리케이션 클라이언트 아이디 값 */
        String clientId = "xIvNWA9wZAWV32E34ttK";

        /* 애플리케이션 클라이언트 시크릿 값 */
        String clientSecret = "ZLKx5j7wef";
        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;

        try {
            text = URLEncoder.encode(pMessage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = post(apiURL, requestHeaders, text);

        System.out.println(responseBody);

        requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(responseBody);
        JSONObject message = (JSONObject) jsonObj.get("message");
        JSONObject result = (JSONObject) message.get("result");
        return (String) result.get("translatedText");
    }

    /* Ref: https://chung-develop.tistory.com/31 */
    private static String post(String apiUrl, Map<String, String> requestHeaders, String text) {
        HttpURLConnection con = connect(apiUrl);

        String postParams = null;

        for(int i = 0; i < text.length(); i++) {
            int index = text.charAt(i);

            if(index >= 65 && index <= 122) {
                /* 영어를 한국어로 바꾸기 */
                postParams ="source=en&target=ko&text=" + text;
            } else {
                /* 한국어를 영어로 바꾸기 */
                postParams = "source=ko&target=en&text=" + text;
            }
        }

        try {
            con.setRequestMethod("POST");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else { // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}

