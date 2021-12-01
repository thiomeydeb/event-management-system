package com.poosh.event.management.utils

class MyUtil {
    static Map flattenListParam(Map map){

        [:].putAll(map.collect{ k, v ->
            def outPut = (String[]) v;
            new MapEntry(k, outPut[0])});
    }
}
