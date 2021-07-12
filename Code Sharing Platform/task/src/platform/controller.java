package platform;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

//limit in num of views
//limit in viewing time
//snippet is deleted when limit is reached

//implement time and views
//decorate restricted_code.ftlh
@Controller
public class controller {
    @Autowired
    CodeService codeService;

    List<Code> codeList = new ArrayList<>(); //stores code snippets in memory

    public void Import(){
        codeList = codeService.getAllCode();
    }

    @GetMapping(value = "/code/{i}", produces = "text/html")
    public String getCode(@PathVariable String i, Model model){
        Import();
        try {
            Code code = findCodeWithID(i);
            model.addAttribute("code", code.getCode());
            model.addAttribute("date", code.getDate());

            if (code.isRestricted()) {
                if(code.getOriginalTime() != 0) {
                    code.setTime(findTimeDiff(code));
                }
                if(code.getOriginalViews() != 0) {
                    code.setViews(code.getViews() - 1);
                }
                if(!code.isExpired()) {
                    if(code.getOriginalTime() != 0) {
                        model.addAttribute("time", code.getTime());
                    }
                    if(code.getOriginalViews() != 0) {
                        model.addAttribute("views", code.getViews());
                    }
                    codeService.saveNewCode(code);
                    return "restricted_code";
                }else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        }catch (NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "code";
    }

    @GetMapping(value = "/api/code/{i}",produces = "application/json")
    @ResponseBody
    public String getCodeJson(@PathVariable String i){
        Import();
        Code returnCode;
        try {returnCode = findCodeWithID(i);}
        catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new Gson().toJson(getCodeAsJsonObj(returnCode));
    }

    @GetMapping(value = "/code/new", produces = "text/html")//same
    public String newCode(){
        Import();
        return "newCode";
    }

    @PostMapping(value = "/api/code/new", produces = "application/json")
    @ResponseBody
    public String postCode(@RequestBody String code){
        Import();
        Code cde = new Code();
        cde.setCode(parseJsonGetCode(code));
        cde.setDate(getTime());

        if(parseJsonGetTime(code) > 0 || parseJsonGetViews(code) > 0){
            cde.setOriginalTime(parseJsonGetTime(code));
            cde.setOriginalViews(parseJsonGetViews(code));
            cde.setRestriction(true);
        }

        String id = randomUUID.getUUID();
        cde.setId(id);
        codeList.add(cde);
        codeService.saveNewCode(cde);
        return "{ \"id\" : \""+id+"\" }";
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

    private String parseJsonGetCode(String Json){
        JsonObject jObj = new JsonParser().parse(Json).getAsJsonObject();
        return jObj.get("code").getAsString();
    }

    private int parseJsonGetTime(String Json){
        JsonObject jObj = new JsonParser().parse(Json).getAsJsonObject();
        return jObj.get("time").getAsInt();
    }

    private int parseJsonGetViews(String Json){
        JsonObject jObj = new JsonParser().parse(Json).getAsJsonObject();
        return jObj.get("views").getAsInt();
    }

    private String recentArrToJson(){
        List<JsonObject> newList = new ArrayList<>();

        int j = 1; //only getting 10 of the latest
        int i = 0;
        while(true){
            if(i >= 10) break;
            if(j > codeList.size()) break;
            Code code = codeList.get(codeList.size()-j);
            if(!code.isRestricted()) {
                newList.add(getCodeAsJsonObj(code));
                i++;
            }
            j++;
        }
        return new Gson().toJson(newList);
    }

    private List<Code> getRecentArr(){
        List<Code> newList = new ArrayList<>();
        int j = 1; //only getting 10 of the latest
        int i = 0;
        while(true){
            if(i >= 10) break;
            if(j > codeList.size()) break;
            Code code = codeList.get(codeList.size()-j);
            if(!code.isRestricted()) {
                newList.add(code);
                i++;
            }
            j++;
        }
        return newList;
    }

    private JsonObject getCodeAsJsonObj(Code code){
        JsonObject jObj = new JsonObject();
        jObj.addProperty("code", code.getCode());
        jObj.addProperty("date", code.getDate());
        if (code.isRestricted()) {
            if(code.getOriginalTime() != 0) {
                code.setTime(findTimeDiff(code));
            }
            if(code.getOriginalViews() != 0) {
                code.setViews(code.getViews() - 1);
            }
            if(!code.isExpired()) {
                jObj.addProperty("time", code.getTime());
                jObj.addProperty("views", code.getViews());
                codeService.saveNewCode(code);
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }else{
            jObj.addProperty("time", code.getTime());
            jObj.addProperty("views", code.getViews());
        }
        return jObj;
    }


    private Code findCodeWithID(String id) throws NotFoundException {
        int a = 0;
        for(; a < codeList.size(); a++){
            if(codeList.get(a).getId().equals(id)){
                break;
            }
        }
        try {
            if (!codeList.get(a).getId().equals(id)) {
                throw new NotFoundException("");
            }
        }catch (IndexOutOfBoundsException e){
            throw new NotFoundException("");
        }
        return codeList.get(a);
    }

    private long findTimeDiff(Code code){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime creationDate = LocalDateTime.parse(code.getDate(), formatter);
        LocalDateTime currentDate = LocalDateTime.now();
        return code.getOriginalTime() - Duration.between(creationDate, currentDate).toSeconds();
    }
}
