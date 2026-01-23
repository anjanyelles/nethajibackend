package com.nethaji.messaging.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TemplateRenderer {

    public String render(String template, Map<String, String> vars) {
        if (template == null) {
            return null;
        }
        String out = template;
        if (vars == null) {
            return out;
        }
        for (Map.Entry<String, String> e : vars.entrySet()) {
            String key = e.getKey();
            String val = e.getValue() == null ? "" : e.getValue();
            out = out.replace("{{" + key + "}}", val);
        }
        return out;
    }
}
