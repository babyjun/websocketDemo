package com.test.websocketDemo.controller;

import com.test.websocketDemo.service.WebSocketServer;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author create by John on 2019/2/20
 * @version 1.0.0
 * @description
 */
@Controller
@RequestMapping("webSocket")
public class WebSocketController {

  @GetMapping("/socket/{cid}")
  public ModelAndView socket(@PathVariable String cid){
    ModelAndView mv= new ModelAndView("/socket");
    mv.addObject("cid",cid);
    return mv;
  }

  @GetMapping("socket/push/{cid}")
  @ResponseBody
  public String push(@PathVariable String cid,String message){
    try {
      WebSocketServer.sendInfo(cid,message);
    } catch (IOException e) {
      e.printStackTrace();
      return cid+"#"+e.getMessage();
    }
    return cid;
  }

}
