/**
 * Copyright (C) 2011 Flamingo Project (http://www.opencloudengine.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.agent.system;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.concurrent.TimeUnit;

import static net.sf.expectit.matcher.Matchers.*;

public class SystemUserServiceImpl implements SystemUserService {

    /**
     * SLF4J Exception Logging
     */
    private Logger exceptionLogger = LoggerFactory.getLogger("flamingo.exception");

    /**
     * SLF4J Application Logging
     */
    private Logger logger = LoggerFactory.getLogger(SystemUserServiceImpl.class);

    private String existCli = "id -u {} &>/dev/null && echo 1";
    private String createCli = "useradd -c {} {}";
    private String changeCli = "passwd {}";
    private String deleteCli = "userdel -r {}";

    @Override
    public boolean existUser(String username) {
        Expect expect = null;
        try {
            Process process = Runtime.getRuntime().exec("/bin/sh");
            String cli = MessageFormatter.arrayFormat(existCli, new String[]{username}).getMessage();
            logger.debug("사용자가 존재하는지 확인합니다. CLI: {}", cli);

            expect = new ExpectBuilder()
                    .withInputs(process.getInputStream())
                    .withOutput(process.getOutputStream())
                    .withTimeout(1, TimeUnit.SECONDS)
                    .withExceptionOnFailure()
                    .build();
            expect.sendLine(cli);
            String result = expect.expect(regexp("\n$")).getBefore();
            expect.sendLine("exit");
            expect.expect(eof());
            process.waitFor();
            expect.close();
            return "1".equals(result.trim());
        } catch (Exception ex) {
            logger.debug("사용자 '{}'가 존재하지 않거나 확인할 수 없습니다.", username);
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });
            return false;
        } finally {
            if (expect != null) {
                try {
                    expect.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    @Override
    public boolean createUser(String home, String name, String username) {
        try {
            String cli = MessageFormatter.arrayFormat(createCli, new String[]{home, name, username}).getMessage();
            logger.debug("사용자를 생성합니다. CLI: {}", cli);
            Process process = Runtime.getRuntime().exec(cli, new String[]{
                    "/bin", "/usr/bin", "/usr/local/bin", "/sbin"
            });

            Expect expect = new ExpectBuilder()
                    .withInputs(process.getInputStream())
                    .withOutput(process.getOutputStream())
                    .withTimeout(3, TimeUnit.SECONDS)
                    .withExceptionOnFailure()
                    .build();

            process.waitFor();
            expect.close();

            logger.debug("사용자 '{}'의 사용자 계정 '{}'을 생성했습니다.", name, username);

            return true;
        } catch (Exception ex) {
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });
            return false;
        }
    }

    @Override
    public boolean changeUser(String username, String password) {
        try {
            String cli = MessageFormatter.arrayFormat(changeCli, new String[]{username}).getMessage();
            logger.debug("사용자의 비밀번호를 설정합니다. CLI: {}", cli);
            Process process = Runtime.getRuntime().exec(cli, new String[]{
                    "/bin", "/usr/bin", "/usr/local/bin", "/sbin"
            });

            Expect expect = new ExpectBuilder()
                    .withInputs(process.getInputStream())
                    .withOutput(process.getOutputStream())
                    .withTimeout(3, TimeUnit.SECONDS)
                    .withExceptionOnFailure()
                    .build();

            expect.sendLine(password);
            expect.sendLine(password);

            process.waitFor();
            expect.close();

            logger.debug("사용자 '{}'의 비밀번호를 설정했습니다.", username);

            return true;
        } catch (Exception ex) {
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });
            return false;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        try {
            String cli = MessageFormatter.arrayFormat(deleteCli, new String[]{username}).getMessage();
            logger.debug("사용자를 삭제합니다. CLI: {}", cli);
            Process process = Runtime.getRuntime().exec(cli, new String[]{
                    "/bin", "/usr/bin", "/usr/local/bin", "/sbin"
            });

            Expect expect = new ExpectBuilder()
                    .withInputs(process.getInputStream())
                    .withOutput(process.getOutputStream())
                    .withTimeout(3, TimeUnit.SECONDS)
                    .withExceptionOnFailure()
                    .build();

            process.waitFor();
            expect.close();

            logger.debug("'{}' 사용자를 삭제했습니다.", username);

            return true;
        } catch (Exception ex) {
            exceptionLogger.warn("{} : {}\n{}", new String[]{
                    ex.getClass().getName(), ex.getMessage(), ExceptionUtils.getFullStackTrace(ex)
            });
            return false;
        }
    }

    public void setExistCli(String existCli) {
        this.existCli = existCli;
    }

    public void setCreateCli(String createCli) {
        this.createCli = createCli;
    }

    public void setChangeCli(String changeCli) {
        this.changeCli = changeCli;
    }

    public void setDeleteCli(String deleteCli) {
        this.deleteCli = deleteCli;
    }
}
