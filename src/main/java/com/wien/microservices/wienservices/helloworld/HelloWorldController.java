package com.wien.microservices.wienservices.helloworld;

import com.wien.microservices.wienservices.helloworld.beans.HelloWorldBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/hello-world-api")
public class HelloWorldController {

    MessageSource messageSource;

    @Autowired
    public HelloWorldController(MessageSource msgSource) {
        this.messageSource = msgSource;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/hello-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }

    @GetMapping("/hello-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBeanPathVariable(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World %s", name));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/greetings")
    public String greetingsToWorld() {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("good.morning.message", null, "Default message", locale);
    }
}
