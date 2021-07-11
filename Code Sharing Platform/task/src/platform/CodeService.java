package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeService {
    @Autowired
    CodeRespository codeRespository;

    public List<Code> getAllCode(){
        List<Code> codeList = new ArrayList<Code>();
        codeRespository.findAll().forEach(code -> {codeList.add(code);});
        return codeList;
    }

    public void saveNewCode(Code code){
        codeRespository.save(code);
    }
}
