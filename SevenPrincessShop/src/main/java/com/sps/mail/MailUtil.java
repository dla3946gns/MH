package com.sps.mail;

import org.apache.commons.mail.HtmlEmail;

//메일을 보내기 위한 설정을 하는 클래스
public class MailUtil {

	//컨트롤러에서 받는 사람의 이메일, 메일제목, 메일내용만 전달받아서 전송할 수 있도록 매개변수로 지정해두었다.
    public static void sendMail(String email, String subject, String msg) throws Exception {
        
        // Mail Server 설정
        String charSet = "utf-8";
        String hostSMTP = com.sps.config.RuntimeConfig.require("MH_SMTP_HOST");       // SMTP 서버명
        String hostSMTPid = com.sps.config.RuntimeConfig.require("MH_SMTP_USERNAME");       // 아이디
        String hostSMTPpwd = com.sps.config.RuntimeConfig.require("MH_SMTP_PASSWORD");    // 비밀번호
        
        // 메일을 보내는 사람
        String fromEmail = com.sps.config.RuntimeConfig.require("MH_SMTP_FROM");
        String fromName = "SevenPrincessShop";
        
        // email 전송
        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setDebug(false);
            mail.setCharset(charSet);
            mail.setSSLOnConnect(false);     // SSL 사용 (TLS가 없는 경우 SSL 사용)
            mail.setStartTLSEnabled(true);
            mail.setStartTLSRequired(true);
            mail.setSSLCheckServerIdentity(true);  // TLS 사용
            mail.setHostName(hostSMTP);
            mail.setSmtpPort(587);          // SMTP 포트 번호
            mail.setAuthentication(hostSMTPid, hostSMTPpwd);
            
            mail.addTo(email);	//이메일 받을 주소
            mail.setFrom(fromEmail, fromName, charSet);//(본인의 이메일 계정, 관리자, "UTF-8")
            mail.setSubject(subject);	//메일제목
            mail.setHtmlMsg(msg);	//본문설정
            mail.send();	//메일전송
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
