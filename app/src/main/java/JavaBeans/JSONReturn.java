package JavaBeans;

import java.util.HashMap;
import java.util.Map;


public class JSONReturn {
    public int errorcode = 202;
    public String message = "成功";
    public Map<String, Object> data = new HashMap<String, Object>();

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public JSONReturn() {
        super();
    }

    public JSONReturn(int errorcode, String message) {
        super();
        this.errorcode = errorcode;
        this.message = message;
    }


    public JSONReturn(int errorcode, String message, Map<String, Object> data) {
        super();
        this.errorcode = errorcode;
        this.message = message;
        this.data = data;
    }


}
