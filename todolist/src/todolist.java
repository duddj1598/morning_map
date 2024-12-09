import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class todolist{
    private String [] todolist;
    private JSONArray todolistarray;
    private JSONObject jsonObject;
    todolist(String id) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: user.json");
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        jsonObject = (JSONObject) parser.parse(reader);
        JSONObject user = (JSONObject) jsonObject.get(id);
        todolistarray = (JSONArray) user.get("todolist");
        this.todolist = new String[todolistarray.size()];
        for (int i = 0; i < todolistarray.size(); i++) {
            JSONObject todo = (JSONObject) todolistarray.get(i);
            this.todolist[i] = (String) todo.get("title");
        }
    }
    public String[] getTodolist(String id) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: user.json");
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        jsonObject = (JSONObject) parser.parse(reader);
        JSONObject user = (JSONObject) jsonObject.get(id);
        todolistarray = (JSONArray) user.get("todolist");
        this.todolist = new String[todolistarray.size()];
        for (int i = 0; i < todolistarray.size(); i++) {
            JSONObject todo = (JSONObject) todolistarray.get(i);
            this.todolist[i] = (String) todo.get("title");
        }
        return this.todolist;
    }
    public boolean[] getdone(String id) throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: user.json");
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        jsonObject = (JSONObject) parser.parse(reader);
        JSONObject user = (JSONObject) jsonObject.get(id);
        todolistarray = (JSONArray) user.get("todolist");
        boolean[] done = new boolean[todolistarray.size()];
        for (int i = 0; i < todolistarray.size(); i++) {
            JSONObject todo = (JSONObject) todolistarray.get(i);
            done[i] = (boolean) todo.get("done");
        }
        return done;
    }
    public void addTodolist(String title){
        JSONObject newtodo = new JSONObject();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dtf.format(Time.get_today());
        newtodo.put("title", title);
        newtodo.put("date", date);
        newtodo.put("done", false);
        todolistarray.add(newtodo);
        try {
            // 외부 경로로 작업할 파일 지정
            File externalFile = new File("user.json");

            // 외부 파일이 없으면, JAR 내부 리소스에서 복사
            if (!externalFile.exists()) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
                if (inputStream != null) {
                    Files.copy(inputStream, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // JSON 데이터를 외부 파일에 저장
            try (FileWriter fileWriter = new FileWriter(externalFile)) {
                fileWriter.write(jsonObject.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void checkTodolist(int finalI) {
        JSONObject todo = (JSONObject) todolistarray.get(finalI);
        todo.put("done", true);
        try {
            // 외부 경로로 작업할 파일 지정
            File externalFile = new File("user.json");

            // 외부 파일이 없으면, JAR 내부 리소스에서 복사
            if (!externalFile.exists()) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
                if (inputStream != null) {
                    Files.copy(inputStream, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // JSON 데이터를 외부 파일에 저장
            try (FileWriter fileWriter = new FileWriter(externalFile)) {
                fileWriter.write(jsonObject.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void uncheckTodolist(int finalI) {
        JSONObject todo = (JSONObject) todolistarray.get(finalI);
        todo.put("done", false);
        try {
            // 외부 경로로 작업할 파일 지정
            File externalFile = new File("user.json");

            // 외부 파일이 없으면, JAR 내부 리소스에서 복사
            if (!externalFile.exists()) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
                if (inputStream != null) {
                    Files.copy(inputStream, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // JSON 데이터를 외부 파일에 저장
            try (FileWriter fileWriter = new FileWriter(externalFile)) {
                fileWriter.write(jsonObject.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}