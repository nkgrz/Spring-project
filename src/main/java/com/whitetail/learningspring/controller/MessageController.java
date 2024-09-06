package com.whitetail.learningspring.controller;

import com.whitetail.learningspring.domain.Message;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import java.util.Map;

@Controller
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String mainMap(
            @RequestParam(required = false, defaultValue = "") String tag,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<Message> page;
        page = messageService.getMessages(tag, pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("tag", tag);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "newTag", required = false) String newTag,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllersUtils.getErrorsMap(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            messageService.addNewMessage(message, newTag, file);
            model.addAttribute("message", null);
        }

        model.addAttribute("url", "/main");
        Pageable firstPageable = PageRequest.of(0, pageable.getPageSize(), Sort.by("id").descending());
        model.addAttribute("page", messageService.findAll(firstPageable));
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
                               @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                               Model model) {
        Page<Message> page = messageService.findMessagesByUser(user.getId(), pageable);
        model.addAttribute("requestedUser", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("url", "/user-messages/" + user.getId());
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", user.equals(currentUser));
        return "userMessages";
    }

    @PostMapping("user-messages/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long user,
                                @RequestParam("id") Message message,
                                @RequestParam("text") String text,
                                @RequestParam("newTag") String tag,
                                @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            messageService.saveMessage(message, text, tag, file, true);
        }
        return "redirect:/user-messages/" + user;
    }

    @PostMapping("/user-messages/delete-msg/{messageId}")
    public String deleteMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long messageId,
                                HttpServletRequest request) {
        messageService.deleteMessage(messageId, currentUser.getId());

        return "redirect:" + request.getHeader("Referer");
    }

}
