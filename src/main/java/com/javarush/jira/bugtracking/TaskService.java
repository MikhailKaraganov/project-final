package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.ActivityRepository;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {

    private final ActivityRepository activityRepository;

    public TaskService(TaskRepository repository, TaskMapper mapper, ActivityRepository activityRepository) {
        super(repository, mapper);
        this.activityRepository = activityRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    @Transactional
    public void addTag(Long taskId, String tag) {
        Task task = repository.getExisted(taskId);
        if (task != null) {
            task.getTags().add(tag);
        }
    }

    public Duration getInProgressTime(long taskId) {
        List<Activity> activities = activityRepository.findAllByTaskId(taskId);
        Duration inProgressTime = null;
        if (!activities.isEmpty()) {
            Optional<Activity> inProgress = getActivitiesWithStatus(activities, "in progress");
            if (activities.size() >= 2) {
                Optional<Activity> ready = getActivitiesWithStatus(activities, "ready");
                inProgressTime = getTimeBetween(inProgress, ready);
            } else {
                inProgressTime = getTimeBetweenNowAndUpdated(inProgress);            }
        }
        return inProgressTime;
    }

    public Duration getTestingTime(long taskId) {
        List<Activity> activities = activityRepository.findAllByTaskId(taskId);
        Duration testingTime = null;
        if (!activities.isEmpty() && activities.size() > 2) {
            Optional<Activity> activityReady = getActivitiesWithStatus(activities, "ready");
            if (activities.size() >= 3) {
                Optional<Activity> activityDone = getActivitiesWithStatus(activities, "done");
                testingTime = getTimeBetween(activityReady, activityDone);
            } else {
                testingTime = getTimeBetweenNowAndUpdated(activityReady);
            }
        }
        return testingTime;
    }


    private static Duration getTimeBetweenNowAndUpdated(Optional<Activity> inProgress) {
        Duration between = null;
        if (inProgress.isPresent()){
            between = Duration.between(LocalDateTime.now(), inProgress.get().getUpdated());
        }
        else throw new IllegalArgumentException("Optional arguments is not present");
        return between;
    }

    private static Duration getTimeBetween(Optional<Activity> firstOptionalActivity, Optional<Activity> secondOptionalActivity) {
        Duration between;
        if(firstOptionalActivity.isPresent() && secondOptionalActivity.isPresent()){
            between = Duration.between(firstOptionalActivity.get().getUpdated(), secondOptionalActivity.get().getUpdated());
        }
        else throw new IllegalArgumentException("Optional arguments is not present");
        return between;
        }



    private static Optional<Activity> getActivitiesWithStatus(List<Activity> activities, String status) {
        return activities.stream().filter(a -> {
                    assert a.getStatusCode() != null;
                    return a.getStatusCode().equals(status);
                })
                .distinct().findFirst();
    }


}