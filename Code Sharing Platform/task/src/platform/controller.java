package platform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    @GetMapping("/code")
    public String returnCode(){
        String code = "<html>\n" +
                "<head>\n" +
                "    <title>Code</title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
                "</head>\n" +
                "<body>\n" +
                "    <pre>\n" +
                "public static void main(String[] args) {\n" +
                "    SpringApplication.run(CodeSharingPlatform.class, args);\n" +
                "}</pre>\n" +
                "</body>\n" +
                "</html>";
        return code;
    }

    @GetMapping("/api/code")
    public String returnJson(){
        String jsonStr = "{\n" +
                "    \"code\": \"public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}\"\n" +
                "}";
        //https://www.baeldung.com/spring-boot-json
        return jsonStr;
    }
}
