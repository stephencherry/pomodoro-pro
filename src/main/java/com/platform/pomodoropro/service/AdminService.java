package com.platform.pomodoropro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.pomodoropro.entity.model.*;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import com.platform.pomodoropro.repository.UserRepository;
import com.platform.pomodoropro.util.AppConstants;
import com.platform.pomodoropro.util.OtpUtil;
import com.platform.pomodoropro.util.mail.EmailService;
import com.platform.pomodoropro.util.mail.Mail;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService{
    private final UserRepository userRepository;
    private final AppConstants constants;
    private final ObjectMapper objectMapper;
    private final OtpUtil otpUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public String signUp(RegistrationRequestBody registrationRequestBody) {
        if (!registrationRequestBody.email.contains("@t-consultonline.com")){
            return null;
        }

        UserEntity existingUser = userRepository.findUserByEmailOrPhone(registrationRequestBody.email, registrationRequestBody.phone);
        if(existingUser!=null && existingUser.getStatus().equals(STATUS.ACTIVATED)){
            return "Email or Phone already taken";
        }

        if (existingUser!=null && existingUser.getStatus().equals(STATUS.CAPTURED)){
            try{
                sendActivationCodeViaMail(existingUser);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            return "Kindly check your mail or phone for OTP to complete registration process.";
        }

        if (existingUser!=null && existingUser.getStatus().equals(STATUS.SUSPENDED)){
            return "Email or Phone has been suspended from this platform";
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setPhone(registrationRequestBody.phone);
        userEntity.setEmail(registrationRequestBody.email);
        userEntity.setGender(GENDER.valueOf(registrationRequestBody.gender));
        userEntity.setFirstname(registrationRequestBody.firstname);
        userEntity.setLastname(registrationRequestBody.lastname);
        userEntity.setRole(ROLE.valueOf(registrationRequestBody.role));
        userEntity.setStatus(STATUS.CAPTURED);
        userEntity.setCreatedDate(OffsetDateTime.now());
        userEntity.setUpdatedDate(OffsetDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(registrationRequestBody.getPassword()));
        userRepository.save(userEntity);

        try{
            sendActivationCodeViaMail(userEntity);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "Kindly check your mail or phone for OTP to complete registration process.";
    }

    @Override
    public ResponseModel getUsers(int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created_date");
        PageRequest pageable = PageRequest.of(page, 50, sort);
        Page<UserEntity> userEntities = userRepository.findNonAdminUsers(pageable);

        List<UserMapper> userMapperList = new ArrayList<>();

        userEntities.forEach(item->{
            UserMapper userMapper = ModelMapper.INSTANCE.mapUserEntityToUserMapper(item);
            userMapper.setCreatedDate(item.getCreatedDate());
            userMapper.setUpdatedDate(item.getUpdatedDate());

            userMapperList.add(userMapper);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("users", userMapperList);
        body.put("total_page", userEntities.getTotalPages());
        body.put("current_page", userEntities.getPageable().getPageNumber());
        body.put("has_next", userEntities.hasNext());

        if(userEntities.hasNext())
            body.put("next_page", constants.BASE_URL+"admin/users/"+userEntities.nextPageable().getPageNumber());
        else
            body.put("next_page", null);

        ResponseModel responseModel = new ResponseModel(body);
        responseModel.setMessage("Success");
        responseModel.setResponseCode("00");
        return responseModel;
    }

    @Override
    public String addUser(RegistrationRequestBody registrationRequestBody) {
        UserEntity existingUser = userRepository.findUserByEmailOrPhone(registrationRequestBody.email, registrationRequestBody.phone);
        if(existingUser!=null && existingUser.getStatus().equals(STATUS.ACTIVATED)){
            return "Email or Phone already exist!";
        }

        if (existingUser!=null && existingUser.getStatus().equals(STATUS.SUSPENDED)){
            return "Email or Phone has been suspended on this platform";
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setFirstname(registrationRequestBody.firstname);
        userEntity.setLastname(registrationRequestBody.lastname);
        userEntity.setPhone(registrationRequestBody.phone);
        userEntity.setEmail(registrationRequestBody.email);
        userEntity.setRole(ROLE.valueOf(registrationRequestBody.role));
        userEntity.setGender(GENDER.valueOf(registrationRequestBody.gender));
        userEntity.setStatus(STATUS.ACTIVATED);
        userEntity.setPassword(passwordEncoder.encode(registrationRequestBody.getPassword()));
        userEntity.setCreatedDate(OffsetDateTime.now());
        userEntity.setUpdatedDate(OffsetDateTime.now());
        userRepository.save(userEntity);
        return "User added successfully";
    }

    private void sendActivationCodeViaMail(UserEntity user) throws MessagingException, TemplateException, IOException {
        int otp = otpUtil.generateOTP(user.getEmail());
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setFrom(constants.MAIL_USERNAME);
        mail.setSubject("Account Activation");

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstname());
        model.put("link", constants.ADMIN_FRONTEND_BASE_URL+"verify?code"+otp);
        model.put("code", ""+otp);
        mail.setModel(model);
        mail.setTemplate("email-activation.ftl");
        emailService.sendSimpleMessage(mail);
    }

    private void sendWelcomeOnBoardMail(UserEntity user) throws MessagingException, TemplateException, IOException {
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setFrom(constants.MAIL_USERNAME);
        mail.setSubject("Welcome Onboard");

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstname());
        model.put("link", constants.ADMIN_FRONTEND_BASE_URL+"login");
        mail.setModel(model);
        mail.setTemplate("admin-welcome.ftl");
        emailService.sendSimpleMessage(mail);
    }
}
