package tasks;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Main test class")
class NewEditControllerTest {
    private NewEditController controller = new NewEditController();
    private String defaultTitle;
    private Date startDate;
    private Date endDate ;
    private int interval;

    private NewEditControllerTest(){
        defaultTitle = "default title";

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 11);
        startDate = cal.getTime();

        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        endDate = cal.getTime();

        interval = 2;
    }


    @Nested
    @DisplayName("ECP test class")
    class CreateTaskECPTest{

        @Tag("title")
        @ParameterizedTest
        @ValueSource(strings = { "titlu valid", "alinaaaaaaaaaaa"})
        void titleValidTest(String title) {
            try {
                controller.createTask(title, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("title")
        @Test
        void titleInvalidTest() {
            assertThrows(TaskException.class, () -> controller.createTask("", startDate, endDate, interval));
        }

        @Tag("date")
        @Test
        void endValidTest() {
            try {
                controller.createTask(defaultTitle, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("date")
        @Test
        void endInvalidTest() {
            assertThrows(TaskException.class, () -> controller.createTask("", endDate, startDate, interval));
        }
    }

    @Nested
    @DisplayName("BVA test class")
    class CreateTaskBVATest{
        @Tag("title")
        @ParameterizedTest
        @ValueSource(strings = { "zilkrqaohrjrjdlxemnyzbokfqlorbqupdhqtfddrxedhaosnkbqcewqjmdhvijtiaojsqdjxgelernmeqiozgwqtevzjmwsnlqywpixlyirprpimwljlqbfafxmeigwjyqueqkakolgueahccqdjcqzzqjgvnlywgnpoddtbpxgkfmzqofkgbknahhcpmxdfaxqfmmytzhsoryegsxfnltxdyzubmkgjsfvpqvckbchsuzckpiqjkkrgddzvpa"})
        void titleValidTest(String title) {
            try {
                controller.createTask(title, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("title")
        @Test
        void titleInvalidTest() {
            String titleInvalid = "zilkrqaohrjrjdlxemnyzbokfqlorbqupdhqtfddrxedhaosnkbqcewqjmdhvijtiaojsqdjxgelernmeqiozgwqtevzjmwsnlqywpixlyirprpimwljlqbfafxmeigwjyqueqkakolgueahccqdjcqzzqjgvnlywgnpoddtbpxgkfmzqofkgbknahhcpmxdfaxqfmmytzhsoryegsxfnltxdyzubmkgjsfvpqvckbchsuzckpiqjkkrgddzvpat";
            assertThrows(TaskException.class, () -> controller.createTask(titleInvalid, startDate, endDate, interval));
        }

        @Tag("date")
        @Test
        void endValidTest() {
            try {
                controller.createTask(defaultTitle, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("date")
        @Test
        void endInvalidTest() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2020);
            cal.set(Calendar.MONTH, Calendar.APRIL);
            cal.set(Calendar.DAY_OF_MONTH, 11);
            cal.set(Calendar.HOUR_OF_DAY, 11);
            cal.set(Calendar.MINUTE, 11);
            cal.set(Calendar.SECOND, 11);
            cal.set(Calendar.MILLISECOND, 11);
            Date startDateInvalid =cal.getTime();

            cal.set(Calendar.MILLISECOND, 10);
            Date endDateInvalid = cal.getTime();

            assertThrows(TaskException.class, () ->
                    controller.createTask(defaultTitle, startDateInvalid, endDateInvalid, interval));
        }
    }
}