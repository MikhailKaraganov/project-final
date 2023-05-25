package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Data
public class UserBelongService {
    private final UserBelongRepository userBelongRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public UserBelong getUserBelongById(long id){
        UserBelong userBelong = userBelongRepository.getExisted(id);
        return userBelong;
    }

    public List<UserBelong> getUserBelongsByUsreID(long id){
        List<UserBelong> userBelongs = userBelongRepository.getByUserId(id);
        return userBelongs;
    }

    @Transactional
    public void saveNewUserBelong(long userId, long taskId){
        List<UserBelong> userBelongList = getUserBelongsByUsreID(userId);
        if (userBelongList.isEmpty()){
            UserBelong userBelong = createUserBelong(userId, taskId);
            userBelongRepository.save(userBelong);
        }
        else{
            long count = userBelongList.stream()
                    .filter(userBelong -> userBelong.getObjectId() == taskId)
                    .count();
            if (count == 0) {
                UserBelong userBelong = createUserBelong(userId, taskId);
                userBelongRepository.save(userBelong);
            }
        }
    }

    private UserBelong createUserBelong(long userId, long taskId){
        UserBelong userBelong = new UserBelong();
        User user = userRepository.getExisted(userId);
        Task task = taskRepository.getExisted(taskId);
        userBelong.setUserId(user.id());
        if (user.getRoles().contains(Role.ADMIN)){
            userBelong.setUserTypeCode(Role.ADMIN.name());
        }
        else {
            userBelong.setUserTypeCode(Role.DEV.name());
        }
        userBelong.setObjectId(task.id());
        userBelong.setObjectType(ObjectType.TASK);
        return userBelong;
    }

}