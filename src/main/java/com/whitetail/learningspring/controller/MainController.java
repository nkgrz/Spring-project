package com.whitetail.learningspring.controller;

import ch.qos.logback.core.util.StringUtil;
import com.whitetail.learningspring.domain.Message;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.service.MessageService;
import com.whitetail.learningspring.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MainController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
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
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllersUtils.getErrorsMap(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            if (file != null && !file.isEmpty()) {
                String fileName = messageService.saveImage(file);
                message.setFilename(fileName);
            }
            messageService.save(message);
            model.addAttribute("message", null);
        }

        model.addAttribute("messages", messageService.findAll());
        return "main";
    }

    @PostMapping("delete")
    public String delete() {
        messageService.deleteMessages();
        return "redirect:/main";
    }

    @GetMapping("user-messages/{user}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                               @RequestParam(required = false) Message message,
                               Model model) {
        model.addAttribute("requestedUser", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", user.getMessages());
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", user.equals(currentUser));
        return "userMessages";
    }

    @PostMapping("user-messages/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long user,
                                @RequestParam("id") Message message,
                                @RequestParam("text") String text,
                                @RequestParam("tag") String tag,
                                @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            boolean isChanged = false;
            if (!StringUtil.isNullOrEmpty(text)) {
                message.setText(text);
                isChanged = true;
            }
            if (!StringUtil.isNullOrEmpty(tag)) {
                message.setTag(tag);
                isChanged = true;
            }
            if (file != null && !file.isEmpty()) {
                messageService.deleteFile(message.getFilename());
                String fileName = messageService.saveImage(file);
                message.setFilename(fileName);
                isChanged = true;
            }

            if (isChanged) {
                messageService.save(message);
            }
        }

        return "redirect:/user-messages/" + user;
    }
}
