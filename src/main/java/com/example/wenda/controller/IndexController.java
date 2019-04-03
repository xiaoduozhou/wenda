package com.example.wenda.controller;

import com.example.wenda.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {
    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        return "hello NowCoder"+httpSession.getAttribute("msg");
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") int groupId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue = "zz") String key){
        //return "profile page"+userId+" "+groupId;
        return String.format("Profile page of %s / %d,t:%d k:%s",groupId,userId,type,key);
    }

    @RequestMapping(path={"/vm"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1","231");
        List<String> colors = Arrays.asList(new String[] {"RED","GREEN"});
        model.addAttribute("colors",colors);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user",new User("Li"));
        return "home";
    }

    @RequestMapping(path={"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String template(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession) {
    StringBuilder sb = new StringBuilder();

    Enumeration<String> headerNames = request.getHeaderNames();
    while(headerNames.hasMoreElements()){
        String name = headerNames.nextElement();
        sb.append(name);
    }
    if(request.getCookies() != null){
        for(Cookie cookie:request.getCookies()){
            sb.append("Cookie:" +cookie.getName()+" values: "+cookie.getValue());
        }
    }
    response.addHeader("nowcoderid","hello");
    response.addCookie(new Cookie("nid","szsfs"));
    return sb.toString();
    }


    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,HttpSession httpSession) {
        httpSession.setAttribute("msg","sredict");
        RedirectView red = new RedirectView("/",true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不正确");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error: " +e.getMessage();
    }
}

