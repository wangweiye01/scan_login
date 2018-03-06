package cn.epaylinks.boot;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.epaylinks.controller.ScanController;

@Controller
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("cn.epaylinks")
public class App {

    @RequestMapping("/")
    String index(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("生成UUID 成功");

        request.setAttribute("uuid", UUID.randomUUID());

        return "pages/index";
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{App.class, ScanController.class}, args);
    }
}
