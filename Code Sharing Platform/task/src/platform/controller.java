package platform;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.ui.Model;

@Controller
public class controller {
    static String code = "public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}";

    @GetMapping(value = "/code", produces = "text/html")
    public String returnCode(Model model){
        model.addAttribute("code", controller.code);
        model.addAttribute("date", getTime());
        return "code";
    }

    @GetMapping(value = "/code/new", produces = "text/html")
    public String newCode(){
        return "newCode";
    }

    @GetMapping(value = "/api/code",produces = "application/json")
    @ResponseBody
    public ApiCode returnJson(){
        return new ApiCode(code, getTime());
    }

    @PostMapping(value = "/api/code/new", produces = "application/json")
    @ResponseBody
    public String updateSnippet(@RequestBody String code){
        controller.code = parseJson(code); //parse Json using Gson here!!!!!
        return "{}";
    }

    private String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }

    private String parseJson(String Json){
        JsonObject jObj = new JsonParser().parse(Json).getAsJsonObject();
        return jObj.get("code").getAsString();
    }
}
