package platform;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

@Controller
public class controller {
    @Autowired
    CodeService codeService;

    List<Code> codeList = new ArrayList<>(); //stores code snippets in memory
    int i = 0;

    public void Import(){
        codeList = codeService.getAllCode();
        i = codeList.get(codeList.size() - 1).getId();
    }

    @GetMapping(value = "/code/{i}", produces = "text/html")
    public String getCode(@PathVariable int i, Model model){
        Import();
        model.addAttribute("code", codeList.get(i-1).getCode());
        model.addAttribute("date", codeList.get(i-1).getDate()); //setTime ... nahh
        return "code";
    }

    @GetMapping(value = "/api/code/{i}",produces = "application/json")
    @ResponseBody
    public Code getCodeJson(@PathVariable int i){
        Import();
        return codeList.get(i-1);
    }

    @GetMapping(value = "/code/new", produces = "text/html")//same
    public String newCode(){
        Import();
        return "newCode";
    }

    @PostMapping(value = "/api/code/new", produces = "application/json")
    @ResponseBody
    public String newSnippet(@RequestBody String code){
        Import();
        i++;
        Code cde = new Code();
        cde.setCode(parseJson(code));
        cde.setDate(getTime());
        cde.setId(i);
        codeList.add(cde);
        codeService.saveNewCode(cde);
        return "{ \"id\" : \""+i+"\" }";
    }

    @GetMapping(value = "/code/latest", produces = "text/html")
    public String latestHTML(Model model){
        Import();
        model.addAttribute("Codes", getRecentArr());
        return "recent";
    }

    @GetMapping(value = "/api/code/latest", produces = "application/json")
    @ResponseBody
    public String latestJson(){
        Import();
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
