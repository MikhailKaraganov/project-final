package com.javarush.jira.profile.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.Profile;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.javarush.jira.profile.web.ProfileRestController.REST_URL;
import static com.javarush.jira.profile.web.ProfileTestInfo.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ProfileMapper mapper;
    @Autowired
    private ProfileRepository repository;

    @Test
    public void tryToGetWithoutAuth_expect401Status() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void tryToGetWithAuth_expectOKStatusAndContent() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(userProfileTo));
    }

    @Test
    @Transactional
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        Profile dbProfileBefore = repository.getExisted(userProfileTo.id());
        ProfileTo dbProfileToBefore = mapper.toTo(dbProfileBefore);
        dbProfileToBefore.setContacts(Set.of(USER_CONTACT_SKYPE, USER_CONTACT_WEBSITE));
        dbProfileToBefore.setMailNotifications(Set.of(ONE_DAY_BEFORE_DEADLINE, DEADLINE));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(dbProfileToBefore, USER_PASSWORD)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Profile dbProfileAfter = repository.getExisted(userProfileTo.id());
        ProfileTo dbProfileToAfter = mapper.toTo(dbProfileAfter);
        TO_MATCHER.assertMatch(dbProfileToAfter, getUpdated());
    }
}
