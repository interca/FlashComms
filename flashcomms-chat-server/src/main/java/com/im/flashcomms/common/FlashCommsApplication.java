package com.im.flashcomms.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author hyj
 * @date 2023-10-6
 */
@SpringBootApplication(scanBasePackages = {"com.im.flashcomms"})
@MapperScan({"com.im.flashcomms.common.**.mapper"})
@ServletComponentScan
public class FlashCommsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashCommsApplication.class,args);
    }

}