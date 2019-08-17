/* $Id$ */
package com.vijay.androidutils.network;


import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Set;


public class RequestPostParams<String,Object> extends MultiValueMap<String,Object> {

    public void addParams(String key, Object value){
        super.put(key,value);
    }
    public void removeParam(Object key) {
        super.remove(key);
    }
    public Set<String> getParamsKeys(){
        return super.keySet();
    }
    public ArrayList<Object> getParams(String key){
        return (ArrayList<Object>) super.get(key);
    }

    public boolean hasParam(String key) {
        return super.containsKey(key);
    }
}
