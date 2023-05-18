package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    
    private final UserBelongRepository userBelongRepository;
    public TaskService(TaskRepository repository, TaskMapper mapper, UserBelongRepository userBelongRepository) {
        super(repository, mapper);
        this.userBelongRepository = userBelongRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    @Transactional
    public void addTag(Long taskId, String tag){
        Task task = repository.getExisted(taskId);
        task.getTags().add(tag);
    }

    @Transactional
    public void assignToUser(Long userId, Long taskId){
        Task task = repository.getExisted(taskId);

    }
}
