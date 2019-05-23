package cn.edu.nju.vivohackathon.tools.network;

import org.junit.Assert;
import org.junit.Test;


public class CookieManagerTest {

    @Test
    public void testAddCookie(){
        CookieManager.setCookie("abc=123asd; qwe=ewq");
        Assert.assertEquals("abc=123asd; qwe=ewq",CookieManager.getCookie());
    }

    @Test
    public void testAdditionalInfo(){
        CookieManager.setCookie("id=a3fWa; Expires=Wed, 21 Oct 2015 07:28:00 GMT; Secure; HttpOnly");
        Assert.assertEquals("id=a3fWa; Expires=Wed, 21 Oct 2015 07:28:00 GMT",CookieManager.getCookie());
    }
}
