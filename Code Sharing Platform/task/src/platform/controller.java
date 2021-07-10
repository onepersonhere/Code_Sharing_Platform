package platform;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.ui.Model;

@Controller
public class controller {
    static final String code = "public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}";

    @GetMapping(value = "/code", produces = "text/html")
    public String returnCode(Model model){
        model.addAttribute("code", controller.code);
        model.addAttribute("date", getTime());
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
