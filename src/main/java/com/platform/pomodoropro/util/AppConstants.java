package com.platform.pomodoropro.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
    @Value("${pomodoro-pro-base.url}")
    public String BASE_URL;

    @Value("${app.email}")
    public String APP_EMAIL;

    @Value("${app.name}")
    public String APP_NAME;

    @Value("${app.description}")
    public String APP_DESCRIPTION;

    @Value("${app.version}")
    public String APP_VERSION;

    @Value("${app.license}")
    public String APP_LICENSE_URL;

    @Value("${admin-frontend-base-url}")
    public String ADMIN_FRONTEND_BASE_URL;

    @Value("${mail.username}")
    public String MAIL_USERNAME;

    @Value("${cloud_front.link}")
    public String CLOUD_FRONT_LINK;

    @Value("${app.social}")
    public String SOCIAL_LOGIN;

    @Value("${ses.key}")
    public String SES_KEY;

    @Value("${ses.secret}")
    public String SES_SECRET;

    @Value("${ses.region}")
    public String SES_REGION;

    private AppConstants() {
    }
}
