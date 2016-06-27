package org.opencloudengine.flamingo2.engine.system;

/**
 * 리눅스 사용자 정보를 관리하기 위한 User Remote Service Interface
 * System User Service Delegator 및 System Agent와 통신한다.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public interface UserRemoteService {

    /**
     * 시스템 사용자가 존재하는지 확인한다.
     *
     * @param username 사용자명
     * @return	true or false
     */
    boolean existUser(String username);

    /**
     * 시스템 사용자를 생성한다.
     *
     * @param username	        사용자명
     * @param name	            이름
     * @param password	        비밀번호
     * @param linuxUserHome	    리눅스 사용자 홈 디렉토리
     * @return	true or false
     */
    boolean createUser(String linuxUserHome, String username, String name, String password);

    /**
     * 시스템 사용자의 비밀번호를 변경한다.
     *
     * @param username	    사용자명
     * @param newPassword	비밀번호
     * @return true or false
     */
    boolean updatePassword(String username, String newPassword);

    /**
     * 시스템 사용자를 삭제한다.
     *
     * @param username	사용자명
     * @return	true or false
     */
    boolean deleteUser(String username);
}