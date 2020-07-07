package com.example.login_register.controller;


import com.example.login_register.model.ConfirmationToken;
import com.example.login_register.model.User;
import com.example.login_register.repository.ConfirmationTokenRepository;
import com.example.login_register.service.EmailSenderService;
import com.example.login_register.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Log4j2
@Controller
@RequestMapping
public class LoginController {

    private final UserService userService;
    private final ConfirmationTokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;

    public LoginController(UserService userService, ConfirmationTokenRepository tokenRepository, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping({"/main", "/"})
    public String getMainPage() {
        return "main-page";
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("registerRq", new User());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String register(/*@Valid @ModelAttribute*/ User user, ModelAndView modelAndView, RedirectAttributes rattrs) {
        log.info(user.getUsername());
        boolean existingUser = userService.findUserByUsername(user.getUsername());
        if(existingUser)
        {
            modelAndView.addObject("message","This email already exists!");
            modelAndView.setViewName("error");
        }
        else
        {
            //userService.save(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            tokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            log.info(user.getUsername());
            mailMessage.setTo(user.getUsername());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("ruslan.hasanzada@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
            emailSenderService.sendEmail(mailMessage);
            //modelAndView.addObject("emailId", user.getEmailId());
            rattrs.addAttribute("email", user.getUsername());
            System.out.println(user.getUsername());
            //modelAndView.setViewName("successfulRegistration");
        }

        return "redirect:successful-registration";
    }

    @RequestMapping(value = "/successful-registration", method = RequestMethod.GET)
    public ModelAndView getSuccessRegister(ModelAndView modelAndView,@ModelAttribute("email") String email){
        modelAndView.addObject("emailId", email);
        log.info(email);
        modelAndView.setViewName("successfulRegistration");
        return modelAndView;
    }



    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView,
                                           @RequestParam("token") String confirmationToken){

        ConfirmationToken token = tokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null){
            User user = userService.findUser(token.getUser().getUsername());
            user.setEnabled(true);
            userService.registerNewUser(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

   /* @GetMapping("/success")
    public String successFullMethod() {
        return "success";
    }*/

    @GetMapping("/landing")
    public String successFullMethod() {
        return "landing";
    }

    @GetMapping("/forgot")
    public String get_forgot_password() {
        return "forgot-password";
    }

}
