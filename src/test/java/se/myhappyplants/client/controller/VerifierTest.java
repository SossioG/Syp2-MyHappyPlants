package se.myhappyplants.client.controller;
import mockcodefiles.RegisterPaneControllerPasswordMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ReqID: ANV12F
 * Lösenord - MUST - Lösenord ska följa NIST (National Institute of Standards and Technology) standard för hur de ska konstrueras. Längden måste vara minst 8 tecken.
 * Innehålla minst en stor bokstav och minst en siffra.
 */

public class VerifierTest {

    RegisterPaneControllerPasswordMock rcm = new RegisterPaneControllerPasswordMock("test@gmail.com",
            "test@gmail.com",
            "testuser",
            "test",
            "test");

    @Test
    void testPasswordLessThen8chars()
    {
        rcm.setPassFldNewPassword("lol");
        rcm.setPassFldNewPassword1("lol");
        Assertions.assertEquals(false,rcm.validateRegistration(rcm));
    }

    @Test
    void testPassword8chars()
    {
        rcm.setPassFldNewPassword("Hahahah1");
        rcm.setPassFldNewPassword1("Hahahah1");
        Assertions.assertEquals(true,rcm.validateRegistration(rcm));
    }

    @Test
    void testPasswordOver8chars()
    {
        rcm.setPassFldNewPassword("Hahahah1234");
        rcm.setPassFldNewPassword1("Hahahah1234");
        Assertions.assertEquals(true,rcm.validateRegistration(rcm));
    }

    @Test
    void testPasswordNoCapitalChar()
    {
        rcm.setPassFldNewPassword("hahahah1234");
        rcm.setPassFldNewPassword1("hahahah1234");
        Assertions.assertEquals(false,rcm.validateRegistration(rcm));
    }

    @Test
    void testPasswordNoNumbers()
    {
        rcm.setPassFldNewPassword("Hahahaha");
        rcm.setPassFldNewPassword1("Hahahaha");
        Assertions.assertEquals(false,rcm.validateRegistration(rcm));
    }

    @Test
    void testEmptyPassword()
    {
        rcm.setPassFldNewPassword("");
        rcm.setPassFldNewPassword1("");
        Assertions.assertEquals(false,rcm.validateRegistration(rcm));
    }
}