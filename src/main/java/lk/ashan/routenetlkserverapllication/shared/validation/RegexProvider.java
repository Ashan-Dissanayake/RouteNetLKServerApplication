package lk.ashan.routenetlkserverapllication.shared.validation;

import jakarta.validation.constraints.Pattern;
import lk.ashan.routenetlkserverapllication.shared.validation.RegexPattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

public class RegexProvider {

    public static <T> HashMap<String, HashMap<String, String>> get(T t) {
        try {
            Class<?> aClass = t.getClass();
            HashMap<String, HashMap<String, String>> regex = new HashMap<>();

            for (Field field : aClass.getDeclaredFields()) {

                Annotation[] annotations = field.getDeclaredAnnotations();

                for (Annotation annotation : annotations) {

                    if (annotation instanceof Pattern myAnnotation) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("regex", myAnnotation.regexp());
                        map.put("message", myAnnotation.message());
                        regex.put(field.getName(), map);
                    }

                    if (annotation instanceof RegexPattern myAnnotation) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("regex", myAnnotation.reg());
                        map.put("message", myAnnotation.msg());
                        regex.put(field.getName(), map);
                    }
                }
            }

            

            return regex;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
