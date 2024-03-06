package com.platform.pomodoropro.service;

import com.platform.pomodoropro.entity.model.*;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import com.platform.pomodoropro.repository.UserRepository;
import com.platform.pomodoropro.util.AppConstants;
import com.platform.pomodoropro.util.OtpUtil;
import com.platform.pomodoropro.util.mail.EmailService;
import com.platform.pomodoropro.util.mail.Mail;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConstants constants;
    private final OtpUtil otpUtil;
    private final EmailService emailService;

    @Override
    public String signUp(RegistrationRequestBody registrationRequestBody) {
        UserEntity existingUser = userRepository.findUserByEmailOrPhone(registrationRequestBody.email, registrationRequestBody.phone);
        if (existingUser != null && existingUser.getStatus().equals(STATUS.ACTIVATED)) {
            return "Email or Phone already exist";
        }
        if (existingUser != null && existingUser.getStatus().equals(STATUS.CAPTURED)) {
            try {
                sendActivationCodeViaMail(existingUser);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return "Kindly check your mail or phone for OTP to complete registration process";
        }
        if (existingUser != null && existingUser.getStatus().equals(STATUS.SUSPENDED)) {
            return "This Email or Phone has been suspended";
        }
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(registrationRequestBody.email);
        userEntity.setFirstname(registrationRequestBody.firstname);
        userEntity.setLastname(registrationRequestBody.lastname);
        userEntity.setPhone(registrationRequestBody.phone);
        userEntity.setGender(GENDER.valueOf(registrationRequestBody.gender));
        userEntity.setStatus(STATUS.CAPTURED);
        userEntity.setCreatedDate(OffsetDateTime.now());
        userEntity.setUpdatedDate(OffsetDateTime.now());

        userEntity.setPassword(passwordEncoder.encode(registrationRequestBody.getPassword()));
        userRepository.save(userEntity);
        try {
            sendActivationCodeViaMail(userEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "Kindly check your mail or phone for OTP to complete registration process";
    }

    @Override
    public String verifySignUp(int code, String email) {
        String message = "Verification Complete";
        if(otpUtil.getOtp(code).equalsIgnoreCase(email)){
            UserEntity userEntity = userRepository.findUserByEmail(email);
            userEntity.setStatus(STATUS.ACTIVATED);
            userEntity.setUpdatedDate(OffsetDateTime.now());
            userRepository.save(userEntity);

            try{
                sendWelcomeOnBoardMail(userEntity);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            message = "Invalid or Expired OTP";
        }
        return message;
    }

    @Override
    public UserMapper getUser(long userId) {
        UserMapper userMapper = new UserMapper();

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if(userEntity.isPresent()){
            userMapper = ModelMapper.INSTANCE.mapUserEntityToUserMapper(userEntity.get());
            userMapper.setCreatedDate(userEntity.get().getCreatedDate());
            userMapper.setUpdatedDate(userEntity.get().getUpdatedDate());
        }
        return userMapper;
    }

    private void sendActivationCodeViaMail(UserEntity user) throws MessagingException, TemplateException,IOException {
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
        mail.setSubject("Welcome on Pomodoro_pro board");

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstname());
        model.put("link", constants.ADMIN_FRONTEND_BASE_URL+"login");
        mail.setModel(model);
        mail.setTemplate("admin-welcome.ftl");
        emailService.sendSimpleMessage(mail);
    }
}


