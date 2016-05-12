/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.web.security;

import org.opencloudengine.flamingo2.util.Encrypter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security Auth Controller
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class AESPasswordEncoder implements PasswordEncoder {

    private String secretKey1;
    private String secretKey2;

    @Override
    public String encode(CharSequence rawPassword) {
        return Encrypter.encrypt(secretKey1, secretKey2, rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String plainText = Encrypter.decrypt(secretKey1, secretKey2, encodedPassword);
        return rawPassword.equals(plainText);
    }

    public String decode(String encodedPassword) {
        return Encrypter.decrypt(secretKey1, secretKey2, encodedPassword);
    }

    public void setSecretKey1(String secretKey1) {
        this.secretKey1 = secretKey1;
    }

    public void setSecretKey2(String secretKey2) {
        this.secretKey2 = secretKey2;
    }
}
