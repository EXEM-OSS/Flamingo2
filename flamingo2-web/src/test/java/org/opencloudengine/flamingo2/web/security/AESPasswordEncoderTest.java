package org.opencloudengine.flamingo2.web.security;

import org.junit.Test;

public class AESPasswordEncoderTest {

    @Test
    public void encode() throws Exception {
        AESPasswordEncoder encoder = new AESPasswordEncoder();
        encoder.setSecretKey1("Bar12345Bar12345");
        encoder.setSecretKey2("ThisIsASecretKet");

        System.out.println(encoder.encode("admin"));
    }

    @Test
    public void matches() throws Exception {
        AESPasswordEncoder encoder = new AESPasswordEncoder();
        encoder.setSecretKey1("Bar12345Bar12345");
        encoder.setSecretKey2("ThisIsASecretKet");

        System.out.println(encoder.matches("admin", "MEVd1+d7s2DoZt8mgx+1kg=="));
    }

    @Test
    public void decode() throws Exception {
        AESPasswordEncoder encoder = new AESPasswordEncoder();
        encoder.setSecretKey1("Bar12345Bar12345");
        encoder.setSecretKey2("ThisIsASecretKet");

        System.out.println(encoder.decode("MEVd1+d7s2DoZt8mgx+1kg=="));
    }
}