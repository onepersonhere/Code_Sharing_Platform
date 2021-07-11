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

@Controller
public class controller {
    static List<Code> codeList = new ArrayList<>(); //stores code snippets in memory
    static int i = 0;

    @GetMapping(value = "/code/{i}", produces = "text/html")
    public String getCode(@PathVariable int i, Model model){
        model.addAttribute("code", codeList.get(i-1).getCode());
        model.addAttribute("date", codeList.get(i-1).getDate()); //setTime ... nahh
        return "code";
    }

    @GetMapping(value = "/api/code/{i}",produces = "application/json")
    @ResponseBody
    public Code getCodeJson(@PathVariable int i){
        return codeList.get(i-1);
    }

    @GetMapping(value = "/code/new", produces = "text/html")//same
    public String newCode(){
        return "newCode";
    }

    @PostMapping(value = "/api/code/new", produces = "application/json")
    @ResponseBody
    public String newSnippet(@RequestBody String code){
        i++;
        codeList.add(new Code(parseJson(code), getTime())); //add Json to latest
        return "{ \"id\" : \""+i+"\" }";
    }

    @GetMapping(value = "/code/latest", produces = "text/html")
    public String latestHTML(Model model){
        model.addAttribute("Codes", getRecentArr());
        return "recent";
    }

    @GetMapping(value = "/api/code/latest", produces = "application/json")
    @ResponseBody
    public String latestJson(){
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
        List<Code> newList = new ArrayList<>();

        int j = 1; //only getting 10 of the latest
        for(int i = 0; i < 10; i++){
            if(j > codeList.size()) break;
            newList.add(codeList.get(codeList.size()-j));
            j++;
        }

        return new Gson().toJson(newList);
    }

    private List<Code> getRecentArr(){
        List<Code> newList = new ArrayList<>();
        int j = 1; //only getting 10 of the latest
        for(int i = 0; i < 10; i++){
            if(j > codeList.size()) break;
            newList.add(codeList.get(codeList.size()-j));
            j++;
        }
        return newList;
    }
}
