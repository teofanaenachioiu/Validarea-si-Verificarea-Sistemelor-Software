package tasks;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Main test class")
class TaskTest {
    private NewEditController controller = new NewEditController();
    private String defaultTitle;
    private Date startDate;
    private Date endDate ;
    private int interval;

    @BeforeEach
    void setUp() {
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
        @ValueSource(strings = { "titlu valid"})
        void titleValidTest(String title) {
            // Title length is between 1 and 255
            try {
                controller.createTask(title, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("title")
        @Test
        void titleInvalid1Test() {
            // Title length is 0
            assertThrows(TaskException.class, () -> controller.createTask("", startDate, endDate, interval));
        }

        @Tag("date")
        @Test
        void endInvalidTest() {
            // End date is before start date
            assertThrows(TaskException.class, () -> controller.createTask(defaultTitle, endDate, startDate, interval));
        }
    }

    @Nested
    @DisplayName("BVA test class")
    class CreateTaskBVATest{
        private Calendar cal = Calendar.getInstance();

        @Tag("title")
        @ParameterizedTest
        @ValueSource(strings = { "zilkrqaohrjrjdlxemnyzbokfqlorbqupdhqtfddrxedhaosnkbqcewqjmdhvijtiaojsqdjxgelernmeqiozgwqtevzjmwsnlqywpixlyirprpimwljlqbfafxmeigwjyqueqkakolgueahccqdjcqzzqjgvnlywgnpoddtbpxgkfmzqofkgbknahhcpmxdfaxqfmmytzhsoryegsxfnltxdyzubmkgjsfvpqvckbchsuzckpiqjkkrgddzvp"})
        void titleValidTest1(String title) {
            // Title length is 254
            try {
                controller.createTask(title, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("title")
        @ParameterizedTest
        @ValueSource(strings = { "zilkrqaohrjrjdlxemnyzbokfqlorbqupdhqtfddrxedhaosnkbqcewqjmdhvijtiaojsqdjxgelernmeqiozgwqtevzjmwsnlqywpixlyirprpimwljlqbfafxmeigwjyqueqkakolgueahccqdjcqzzqjgvnlywgnpoddtbpxgkfmzqofkgbknahhcpmxdfaxqfmmytzhsoryegsxfnltxdyzubmkgjsfvpqvckbchsuzckpiqjkkrgddzvpa"})
        void titleValidTest2(String title) {
            // Title length is 255
            try {
                controller.createTask(title, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("title")
        @ParameterizedTest
        @ValueSource(strings = { "A"})
        void titleValidTest3(String title) {
            // Title length is 1
            try {
                controller.createTask(title, startDate, endDate, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("title")
        @Test
        void titleInvalidTest2() {
            // Title length is 256
            String titleInvalid = "zilkrqaohrjrjdlxemnyzbokfqlorbqupdhqtfddrxedhaosnkbqcewqjmdhvijtiaojsqdjxgelernmeqiozgwqtevzjmwsnlqywpixlyirprpimwljlqbfafxmeigwjyqueqkakolgueahccqdjcqzzqjgvnlywgnpoddtbpxgkfmzqofkgbknahhcpmxdfaxqfmmytzhsoryegsxfnltxdyzubmkgjsfvpqvckbchsuzckpiqjkkrgddzvpat";
            assertThrows(TaskException.class, () -> controller.createTask(titleInvalid, startDate, endDate, interval));
        }

        @Tag("date")
        @Test
        void endValidTest() {
            // Difference between start date and end date is one minute
            cal.set(Calendar.YEAR, 2021);
            cal.set(Calendar.MONTH, Calendar.MAY);
            cal.set(Calendar.DAY_OF_MONTH, 12);
            cal.set(Calendar.HOUR_OF_DAY, 12);
            cal.set(Calendar.MINUTE, 12);
            Date startDateInvalid = cal.getTime();

            cal.set(Calendar.MINUTE, 13);
            Date endDateInvalid = cal.getTime();

            try {
                controller.createTask(defaultTitle, startDateInvalid, endDateInvalid, interval);
            } catch (TaskException e) {
                fail();
            }
        }

        @Tag("date")
        @Test
        void endInvalidTest1() {
            // End date and Start date are the same
            cal.set(Calendar.YEAR, 2020);
            cal.set(Calendar.MONTH, Calendar.APRIL);
            cal.set(Calendar.DAY_OF_MONTH, 11);
            cal.set(Calendar.HOUR_OF_DAY, 11);
            cal.set(Calendar.MINUTE, 11);
            Date dateInvalid = cal.getTime();

            assertThrows(TaskException.class, () ->
                    controller.createTask(defaultTitle, dateInvalid, dateInvalid, interval));
        }

        @Tag("date")
        @Test
        void endInvalidTest2() {
            // Difference between start date and end date is one minute (but end date is before)
            cal.set(Calendar.YEAR, 2022);
            cal.set(Calendar.MONTH, Calendar.JULY);
            cal.set(Calendar.DAY_OF_MONTH, 13);
            cal.set(Calendar.HOUR_OF_DAY, 13);
            cal.set(Calendar.MINUTE, 13);

            Date startDateInvalid = cal.getTime();

            cal.set(Calendar.MINUTE, 12);
            Date endDateInvalid = cal.getTime();

            assertThrows(TaskException.class, () ->
                    controller.createTask(defaultTitle, startDateInvalid, endDateInvalid, interval));
        }
    }
}