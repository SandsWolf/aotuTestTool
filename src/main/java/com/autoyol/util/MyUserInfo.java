package com.autoyol.util;

import com.jcraft.jsch.*;

public class MyUserInfo implements UserInfo {
//    @Override
//    public String getPassphrase() {
//        // TODO Auto-generated method stub
//        System.out.println("MyUserInfo.getPassphrase()");
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        // TODO Auto-generated method stub
//        System.out.println("MyUserInfo.getPassword()");
//        return null;
//    }
//
//    @Override
//    public boolean promptPassphrase(String arg0) {
//        // TODO Auto-generated method stub
//        System.out.println("MyUserInfo.promptPassphrase()");
//        System.out.println(arg0);
//        return false;
//    }
//
//    @Override
//    public boolean promptPassword(String arg0) {
//        // TODO Auto-generated method stub
//        System.out.println("MyUserInfo.promptPassword()");
//        System.out.println(arg0);
//        return false;
//    }
//
//    @Override
//    public boolean promptYesNo(String arg0) {
//        // TODO Auto-generated method stub'
//        System.out.println("MyUserInfo.promptYesNo()");
//        System.out.println(arg0);
//        if (arg0.contains("The authenticity of host")) {
//            return true;
//        }
//        return true;
//    }
//
//    @Override
//    public void showMessage(String arg0) {
//        // TODO Auto-generated method stub
//        System.out.println("MyUserInfo.showMessage()");
//    }
    private String passphrase = null;

    public MyUserInfo() {
    }

    public MyUserInfo(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getPassword() {
        return null;
    }

    public boolean promptPassphrase(String s) {
        return true;
    }

    public boolean promptPassword(String s) {
        return true;
    }

    public boolean promptYesNo(String s) {
        return true;
    }

    public void showMessage(String s) {
        System.out.println(s);
    }



    public static void main(String[] args) {
        String keyFile = "./id_rsa";
        String user = "lvxiang";
        String host = "10.0.3.205";
        String passphrase = "";
        int port = 3721;
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(keyFile);

            Session session = jsch.getSession(user, host, port);

            // username and passphrase will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo(passphrase);
            session.setUserInfo(ui);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            System.out.println(sftp.pwd());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
