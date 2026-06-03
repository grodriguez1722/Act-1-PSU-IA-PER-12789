package com.example.demo;

import org.junit.jupiter.api.Test;

public class DemoApplicationMainTest {

    @Test
    void main() {
        DemoApplication.main(new String[] { "--spring.main.web-application-type=none" });
    }
}
