package allen.frame.entry;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {
    private String code;
    private String name;
    private List<City> area;

    public City() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getArea() {
        return area;
    }

    public void setArea(List<City> area) {
        this.area = area;
    }
}
