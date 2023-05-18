package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.to.SprintTo;
import com.javarush.jira.bugtracking.to.TaskTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping
public class DashboardUIController {

    private TaskService taskService;
    private UserBelongService userBelongService;

    @GetMapping("/") // index page
    public String getAll(Model model) {
        List<TaskTo> tasks = taskService.getAll();
        Map<SprintTo, List<TaskTo>> taskMap = tasks.stream()
                .collect(Collectors.groupingBy(TaskTo::getSprint));
        model.addAttribute("taskMap", taskMap);
        return "index";
    }

    @GetMapping("/api/add_tag/{id}/{tag}")
    public String addTag(@PathVariable("id") Long id, @PathVariable("tag") String tag, Model model){
        taskService.addTag(id, tag);
        return getAll(model);
    }

    @GetMapping("/api/addBelongTo/{userId}/{taskId}")
    public String addBelongTo(@PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId, Model model){
        userBelongService.saveNewUserBelong(userId,taskId);
        return getAll(model);
    }
}
