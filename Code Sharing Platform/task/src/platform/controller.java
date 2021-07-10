package platform;

import jdk.jfr.ContentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
public class controller {
    static String code = "public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}";

    @GetMapping("/code")
    public String returnCode(){
        String str = "<html>\n" +
                "<head>\n" +
                "    <title>Code</title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
                "</head>\n" +
                "<body>\n" +
                "<pre id=\"code_snippet\">\n" +
                code +"\n" +
                "</pre>\n" +
                "</body>\n" +
                "</html>";
        return str;
    }

    @GetMapping("/api/code")
    public String returnJson(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String jsonStr = "{\n" +
                "    \"code\": \""+code+"\"\n" +
                "    \"date\": \""+date+"\"\n" +
                "}";
        //https://www.baeldung.com/spring-boot-json
        return jsonStr;
    }
}
