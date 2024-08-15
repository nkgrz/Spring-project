package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.domain.Message;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    private final MessageService messageService;

    @Autowired
    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String mainMap(@RequestParam(required = false, defaultValue = "") String tag, Model model) {
        List<Message> messages;
        if (tag != null && !tag.isEmpty()) {
            messages = messageService.findByTag(tag);
        } else {
            messages = messageService.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("tag", tag);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws IOException {
        Message message = new Message(text, tag, user);
        if (file != null && !file.isEmpty()) {
            String fileName = messageService.saveImage(file);
            message.setFilename(fileName);
        }
        messageService.save(message);
        model.addAttribute("messages", messageService.findAll());
        return "main";
    }

    @PostMapping("delete")
    public String delete() {
        messageService.deleteMessages();
        return "redirect:/main";
    }
}
