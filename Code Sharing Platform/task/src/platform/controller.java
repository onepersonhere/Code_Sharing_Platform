package platform;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

//reminder: create a class called code to add code and time tgt
@Controller
public class controller {
    static List<String> code = new ArrayList<>();
    static List<ApiCode> recent = new ArrayList<>();
    static int i = 0;

    @GetMapping(value = "/code/{i}", produces = "text/html")
    public String returnCode(@PathVariable int i, Model model){
        model.addAttribute("code", code.get(i-1));
        model.addAttribute("date", getTime());
        return "code";
    }

    @GetMapping(value = "/code/new", produces = "text/html")//same
    public String newCode(){
        return "newCode";
    }

    @GetMapping(value = "/code/latest", produces = "text/html")
    public String latestHTML(){
        return "WIP";
    }

    @GetMapping(value = "/api/code/{i}",produces = "application/json")
    @ResponseBody
    public ApiCode returnJson(@PathVariable int i){
        return new ApiCode(code.get(i-1), getTime());
    }

    @PostMapping(value = "/api/code/new", produces = "application/json")
    @ResponseBody
    public String updateSnippet(@RequestBody String code){
        i++;
        controller.code.add(parseJson(code)); //add Json to latest
        recent.add(new ApiCode(parseJson(code), getTime()));
        return "{ \"id\" : \""+i+"\" }";
    }

    @GetMapping(value = "/api/code/latest", produces = "application/json")
    @ResponseBody
    public String apiLatest(){
        return recentArrToJson(); //assume the latest time is the time of upload & not the time of access
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

    private String recentArrToJson(){
        List<ApiCode> newList = new ArrayList<>();

        int j = 1; //only getting 10 of the latest
        for(int i = 0; i < 10; i++){
            if(j > recent.size()) break;
            newList.add(recent.get(recent.size()-j));
            j++;
        }

        return new Gson().toJson(newList);
    }
}
