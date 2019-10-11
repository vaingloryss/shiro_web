package com.vainglory.controller;

import com.vainglory.domain.ScoreItem;
import com.vainglory.domain.User;
import com.vainglory.service.ScoreService;
import com.vainglory.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/userController/")
public class UserController {

    @Autowired
    ScoreService scoreService;

    @Autowired
    UserService userService;

    @GetMapping("login")
    public String toLogin(){
        return "login";
    }
    @GetMapping("error")
    public String error(){
        return "error";
    }

    @GetMapping("updateScore")
    public String toUpdateScore(Integer scoreId,Model model){
        ScoreItem scoreItem = scoreService.findById(scoreId);
        model.addAttribute("scoreItem", scoreItem);
        return "update";
    }

    @RequestMapping("getScoreList")
    public String getScoreList(Model model){
        List<ScoreItem> scoreItemList = scoreService.findAll();
        model.addAttribute("scoreItemList", scoreItemList);
        return "scoreList";
    }

    @PostMapping("login")
    public String login(User user){
        System.out.println("login ...");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        return "forward:getScoreList";
    }

    @GetMapping("deleteScore")
    public String deleteScore(Integer scoreId){
        scoreService.deleteById(scoreId);
        return "forward:getScoreList";
    }

    @PostMapping("updateScore")
    public String updateScore(ScoreItem scoreItem){
        scoreService.updateScore(scoreItem);
        return "forward:getScoreList";
    }

    @GetMapping("register")
    public String toRegister(){
        return "register";
    }
    @PostMapping("register")
    public String register(User user){
        userService.register(user);
        return "redirect:login";
    }
}
