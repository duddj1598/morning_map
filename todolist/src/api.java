import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Time {
    public static LocalDate get_today() {
        LocalDate today = LocalDate.now();
        return today;
    }
    public static LocalTime get_time() {
        LocalTime time = LocalTime.now();
        return time;
    }
}

public class api {
    final private String api_key = "j35h96igQPhnOCEjnpHsU085rhOZDOK6mHXUToXN7hqOFxBJh79WOJc%2FrK8iZLEPfnbLkLnrmsuediWVsyeJPQ%3D%3D";
    private String date;
    private String time;
    private String temperature;
    private String humidity;
    private String [] pty =  new String[3];
    private String [] weather =  new String[7];
    private String [] rain = new String[3];
    private String [] sky =  new String[3];
    private String [] N_temperature =  new String[7];
    private String [] X_temperature = new String[7];
    private String [] weatherimg = new String[7];
    String get_date(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter houris = DateTimeFormatter.ofPattern("HH");
        String hour = Time.get_time().format(houris);
        if(Integer.parseInt(hour)<5){
            date = Time.get_today().minusDays(1).format(formatter);
        }
        else {
            date = Time.get_today().format(formatter);
        }
        return date;
    }
    public String [] get_weather(){
        return weather;
    }
    public String get_temperature(){
        return temperature;
    }
    public String [] getN_temperature(){
        return N_temperature;
    }
    public String [] getX_temperature(){
        return X_temperature;
    }
    public String [] get_weatherimg(){
        return weatherimg;
    }
    String gettime(){
        DateTimeFormatter minis = DateTimeFormatter.ofPattern("mm");
        DateTimeFormatter houris = DateTimeFormatter.ofPattern("HH");
        String hour = Time.get_time().format(houris);
        String min = Time.get_time().format(minis);
        if(Integer.parseInt(min)<30){
            hour = Integer.toString(Integer.parseInt(hour)-1);
            min = "30";
        }
        else{
            min = "00";
        }
        String givetime = hour+min;
        return givetime;
    }
    String now_time(){
        DateTimeFormatter houris = DateTimeFormatter.ofPattern("HH");
        String now_time = Time.get_time().format(houris);
        now_time = Integer.toString(Integer.parseInt(now_time)+1)+"00";
        return now_time;

    }
    api(){
        try{
            String apiUrlnow = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey="+api_key+"&pageNo=1&numOfRows=1000&dataType=JSON&base_date="+get_date()+"&base_time="+gettime()+"&nx=55&ny=127";
            String apiUrltwo = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey="+api_key+"&pageNo=1&numOfRows=1000&dataType=JSON&base_date="+get_date()+"&base_time=0500&nx=60&ny=127";
            String apiUrlweather = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?serviceKey="+api_key+"&pageNo=1&numOfRows=10&dataType=JSON&regId=11B00000&tmFc="+get_date()+"0600";
            String apiUrltemp = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?serviceKey="+api_key+"&pageNo=1&numOfRows=10&dataType=JSON&regId=11B10101&tmFc="+get_date()+"0600";
            HttpClient client = HttpClient.newHttpClient();

            // 현재 날씨
            HttpRequest requestnow = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrlnow))
                    .GET()
                    .build();
            HttpResponse<String> responsenow = client.send(requestnow, HttpResponse.BodyHandlers.ofString());
            JSONParser parser = new JSONParser();
            JSONObject jsonResponsenow = (JSONObject) parser.parse(responsenow.body());
            JSONObject responseObjectnow = (JSONObject) jsonResponsenow.get("response");
            JSONObject bodyObjectnow = (JSONObject) responseObjectnow.get("body");
            JSONObject itemsObjectnow = (JSONObject) bodyObjectnow.get("items");
            JSONArray itemArraynow = (JSONArray) itemsObjectnow.get("item");
            for(int i=0; i<itemArraynow.size(); i++){
                JSONObject item = (JSONObject) itemArraynow.get(i);
                String fcstTime = (String) item.get("fcstTime");
                if(fcstTime.equals(now_time())){
                    String category = (String) item.get("category");
                    switch (category) {
                        case "T1H":
                            temperature = (String) item.get("fcstValue");
                            break;
                        case "RN1":
                            rain[0] = (String) item.get("fcstValue");
                            break;
                        case "SKY":
                            sky[0] = (String) item.get("fcstValue");
                            break;
                        case "REH":
                            humidity = (String) item.get("fcstValue");
                            break;
                        case "PTY":
                            pty[0] = (String) item.get("fcstValue");
                            break;
                        default:
                            break;
                    }
                }
            }

            // 이틀 후 날씨
            HttpRequest requesttwo = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrltwo))
                    .GET()
                    .build();
            HttpResponse<String> responsetwo = client.send(requesttwo, HttpResponse.BodyHandlers.ofString());

            // JSON 응답 파싱
            JSONObject jsonResponsetwo = (JSONObject) parser.parse(responsetwo.body());
            JSONObject responseObjecttwo = (JSONObject) jsonResponsetwo.get("response");
            JSONObject bodyObjecttwo = (JSONObject) responseObjecttwo.get("body");
            JSONObject itemsObjecttwo = (JSONObject) bodyObjecttwo.get("items");
            JSONArray itemArraytwo = (JSONArray) itemsObjecttwo.get("item");
            for(int i=0; i<itemArraytwo.size(); i++){
                JSONObject itemtwo = (JSONObject) itemArraytwo.get(i);
                String fcstDatetwo = (String) itemtwo.get("fcstDate");
                if(fcstDatetwo.equals(get_date())){
                    String category = (String) itemtwo.get("category");
                    if(category.equals("TMN")) N_temperature[0] = (String) itemtwo.get("fcstValue");
                    if(category.equals("TMX")) X_temperature[0] = (String) itemtwo.get("fcstValue");
                }
                for(int j=0; j<3; j++){
                    //fcstDatetwo가 get_date()의 i일 후와 같으면
                   if(fcstDatetwo.equals(Time.get_today().plusDays(j).format(DateTimeFormatter.ofPattern("yyyyMMdd")))){
                        String category = (String) itemtwo.get("category");
                        switch (category) {
                            case "RN1":
                                rain[j] = (String) itemtwo.get("fcstValue");
                                break;
                            case "SKY":
                                sky[j] = (String) itemtwo.get("fcstValue");
                                break;
                            case "PTY":
                                pty[j] = (String) itemtwo.get("fcstValue");
                                break;
                            case "TMN":
                                N_temperature[j] = (String) itemtwo.get("fcstValue");
                                break;
                            case "TMX":
                                X_temperature[j] = (String) itemtwo.get("fcstValue");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            for(int i=0; i<3; i++){
                switch (pty[i]) {
                    case "0" -> {
                        switch (sky[i]) {
                            case "1" -> {
                                weather[i] = "맑음";
                                weatherimg[i] = "./img/sun.png";
                            }
                            case "3" -> {
                                weather[i] = "구름많음";
                                weatherimg[i] = "./img/cloud.png";
                            }
                            case "4" -> {
                                weather[i] = "흐림";
                                weatherimg[i] = "./img/cloudy.png";
                            }
                        }
                    }

                    case "1" -> {
                        weather[i] = "비";
                        weatherimg[i] = "./img/rain.png";
                    }
                    case "2" -> {
                        weather[i] = "비/눈";
                        weatherimg[i] = "./img/rain_snow.png";
                    }
                    case "3" -> {
                        weather[i] = "눈";
                        weatherimg[i] = "./img/snow.png";
                    }
                    default -> weather[i] = "없음";
                }
            }

            //3일 후 날씨
            HttpRequest requestweather = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrlweather))
                    .GET()
                    .build();
            HttpResponse<String> responseweather = client.send(requestweather, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponseweather = (JSONObject) parser.parse(responseweather.body());
            JSONObject responseObjectweather = (JSONObject) jsonResponseweather.get("response");
            JSONObject bodyObjectweather = (JSONObject) responseObjectweather.get("body");
            JSONObject itemsObjectweather = (JSONObject) bodyObjectweather.get("items");
            JSONArray itemArrayweather = (JSONArray) itemsObjectweather.get("item");
            JSONObject itemweather = (JSONObject) itemArrayweather.get(0);
            for(int i=3; i<7; i++){
                weather[i] = (String) itemweather.get("wf"+(i+1)+"Am");
                switch (weather[i]) {
                    case "맑음":
                        weatherimg[i] = "./img/sun.png";
                        break;
                    case "구름많음":
                        weatherimg[i] = "./img/cloud.png";
                        break;
                    case "흐림":
                        weatherimg[i] = "./img/cloudy.png";
                        break;
                    case "구름많고 비":
                    case "흐리고 비":
                    case "구름많고 소나기":
                    case "소나기":
                        weatherimg[i] = "./img/rain.png";
                        break;
                    case "구름많고 눈":
                    case "흐리고 눈":
                        weatherimg[i] = "./img/snow.png";
                        break;
                    case "흐리고 비/눈":
                    case "구름많고 비/눈":
                        weatherimg[i] = "./img/rain_snow.png";
                        break;
                }
            }

            //3일 후 기온
            HttpRequest requesttemp = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrltemp))
                    .GET()
                    .build();
            HttpResponse<String> responsetemp = client.send(requesttemp, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponsetemp = (JSONObject) parser.parse(responsetemp.body());
            JSONObject responseObjecttemp = (JSONObject) jsonResponsetemp.get("response");
            JSONObject bodyObjecttemp = (JSONObject) responseObjecttemp.get("body");
            JSONObject itemsObjecttemp = (JSONObject) bodyObjecttemp.get("items");
            JSONArray itemArraytemp = (JSONArray) itemsObjecttemp.get("item");
            JSONObject itemtemp = (JSONObject) itemArraytemp.get(0);
            for(int i=3; i<7; i++){
                N_temperature[i] = itemtemp.get("taMin"+(i+1)).toString();
                X_temperature[i] = itemtemp.get("taMax"+(i+1)).toString();
            }
            DateTimeFormatter today_1 = DateTimeFormatter.ofPattern("YYYY년 MM월");
            DateTimeFormatter today_2 = DateTimeFormatter.ofPattern("dd");
            String today = Time.get_today().format(today_1);
            String today_day = Time.get_today().format(today_2);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

class news_api{
    final private String api_key = "cffaa97f7c2f4ad0bd501ce7cd00b7e1";
    private String date_from;
    private String [] news_title = new String[2];
    private String [] news_url = new String[2];
    private String [] news_img = new String[2];
    String get_date(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date_from = Time.get_today().format(formatter);
        return date_from;
    }
    news_api(){
        try {
            // API URL 설정
            String apiUrl = "https://api-v2.deepsearch.com/v1/articles?date_from="+get_date()+"&date_to="+get_date()+"&api_key="+api_key;
            // HTTP 클라이언트 생성
            HttpClient client = HttpClient.newHttpClient();

            // HTTP 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // 요청 보내고 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // JSON 응답 파싱
            JSONParser parser = new JSONParser();
            JSONObject jsonResponsetwo = (JSONObject) parser.parse(response.body());
            JSONArray dataArray = (JSONArray) jsonResponsetwo.get("data");

            for (int i = 0; i < 2; i++) {
                JSONObject item = (JSONObject) dataArray.get(i);
                news_title[i] = (String) item.get("title");
                news_url[i] = (String) item.get("content_url");
                news_img[i] = (String) item.get("image_url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String[] get_news_title(){
        return news_title;
    }
    public String[] get_news_url(){
        return news_url;
    }
    public String[] get_news_img(){
        return news_img;
    }
}
