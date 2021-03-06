package functional;

import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

/**
 * Created by taosong on 25/09/2014.
 */
public class AuthenticatingControllerTest extends FunctionalTest {

    @Ignore
    @Test
    public void testReadCookieAndRedirectToDM() {
        Http.Response response = GET("/");
        assertEquals("https://digitalmarketplace.service.gov.uk/digitalmarketplace", response.getHeader("Location"));
    }
}
