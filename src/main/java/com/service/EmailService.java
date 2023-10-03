package com.service;

import com.domain.User;
import com.repository.UserRepository;
import org.springframework.stereotype.Service;



@Service
public class EmailService {
    private final MailService mailService;
    private final UserRepository userRepository;

    public EmailService(MailService mailService, UserRepository userRepository) {
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    public void sendEmail(){
        User user = userRepository.findOneByLogin("abanmy").orElseThrow(UsernameAlreadyUsedException::new);
        mailService.sendEmailFromTemplate(user, "mail/generateForms", "email.activation.title");
    }
//    private final JavaMailSender javaMailSender;
//
//    public EmailService(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//    public void sendEmail() throws MessagingException, IOException {
////        Model model = new Model();
////        templateEngine.process
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true);
//
//        mimeMessageHelper.setFrom("wzwz6aa3@gmail.com");
//        mimeMessageHelper.setTo("ebnabanmy@gmail.com");
//        mimeMessageHelper.setSubject("Test Email sender, and change mail");
//        mimeMessageHelper.setText(htmlToString("src/main/resources/templates/mail/activationEmail.html"), true);
////        mimeMessageHelper.setText();
//
//        javaMailSender.send(message);
//    }

//    private String htmlToString(String filePath) throws IOException {
//        String htmlContent = FileUtils.readFileToString(new File(filePath));
//        return htmlContent;    }
}
