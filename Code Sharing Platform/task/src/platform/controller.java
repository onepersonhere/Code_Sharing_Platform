package platform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class controller {
    static String code = "public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}";

    @GetMapping(value = "/code", produces = "text/html")
    public String returnCode(){
        return "code";
    }

    @GetMapping(value = "/api/code",produces = "application/json")
    @ResponseBody
    public ApiCode returnJson(){
        return new ApiCode(code, getTime());
    }

    private String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }
}
