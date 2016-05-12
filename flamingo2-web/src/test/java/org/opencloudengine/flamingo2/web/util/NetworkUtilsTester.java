package org.opencloudengine.flamingo2.web.util;

/**
 * System.setProperty("java.net.preferIPv4Stack" , "true");
 */
public class NetworkUtilsTester {

    public static void main(String args[]) {
        System.out.println("NIC MAC Address : " + NetworkUtils.getMacAddress());
        System.out.println("NIC Display Name : " + NetworkUtils.getDisplayName());
        System.out.println("NIC Name : " + NetworkUtils.getName());
    }

}
