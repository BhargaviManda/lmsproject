package com.thbs.BatchManagement.entity;


import java.net.InetAddress;
import java.net.UnknownHostException;
 
public class IPAddressUtil {
 
    public static String getLocalIPAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        String ipAddress = IPAddressUtil.getLocalIPAddress();
        System.out.println("Local IP Address: " + ipAddress);
    }
}
