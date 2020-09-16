package cn.allen.ems.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data<T> implements Serializable {
    private int count;
    private List<T> list;

    public Data() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getList() {
        if(list==null){
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
