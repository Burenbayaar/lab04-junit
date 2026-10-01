package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("GradeCalculator-ийн нэгжийн тестүүд")
class GradeCalculatorTest {

    private static final double DELTA = 1e-9;
    private GradeCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new GradeCalculator(); // Arrange
    }

    @Test
    @DisplayName("Ердийн утгууд: 95→A, 85→B, 75→C, 65→D, 30→F")
    void typicalScoresGiveExpectedGrades() {
        // Act
        String g95 = calc.letterGrade(95);
        String g85 = calc.letterGrade(85);
        String g75 = calc.letterGrade(75);
        String g65 = calc.letterGrade(65);
        String g30 = calc.letterGrade(30);
        // Assert
        assertAll("ердийн дүнгүүд",
                () -> assertEquals("A", g95),
                () -> assertEquals("B", g85),
                () -> assertEquals("C", g75),
                () -> assertEquals("D", g65),
                () -> assertEquals("F", g30));
    }

    @Test
    @DisplayName("90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)")
    void ninetyIsExactlyA() {
        double score = 90.0;                    // Arrange
        String grade = calc.letterGrade(score); // Act
        assertEquals("A", grade);               // Assert
    }

    @Test
    @DisplayName("89.99 оноо B дүн байх ёстой (A-гийн хязгаарын яг доор)")
    void justBelowNinetyIsB() {
        double score = 89.99;
        String grade = calc.letterGrade(score);
        assertEquals("B", grade);
    }

    @Test
    @DisplayName("60 оноо яг D дүн байх ёстой (тэнцэх доод хязгаар)")
    void sixtyIsExactlyD() {
        double score = 60.0;
        String grade = calc.letterGrade(score);
        assertEquals("D", grade);
    }

    @Test
    @DisplayName("59.99 оноо F дүн байх ёстой (тэнцэх хязгаарын яг доор)")
    void justBelowSixtyIsF() {
        double score = 59.99;
        String grade = calc.letterGrade(score);
        assertEquals("F", grade);
    }

    @Test
    @DisplayName("0 оноо F дүн байх ёстой (доод зах)")
    void zeroIsF() {
        double score = 0.0;
        String grade = calc.letterGrade(score);
        assertEquals("F", grade);
    }

    @Test
    @DisplayName("100 оноо A дүн байх ёстой (дээд зах)")
    void hundredIsA() {
        double score = 100.0;
        String grade = calc.letterGrade(score);
        assertEquals("A", grade);
    }

    @Test
    @DisplayName("-1 оноо өгвөл IllegalArgumentException шидэх ёстой")
    void negativeScoreThrows() {
        double score = -1;
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(score));
    }

    @Test
    @DisplayName("101 оноо өгвөл IllegalArgumentException шидэх ёстой")
    void scoreAboveHundredThrows() {
        double score = 101;
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(score));
    }

    @ParameterizedTest(name = "[{index}] {0} оноо → exception")
    @ValueSource(doubles = {-0.01, -100, 100.01, 1000, Double.NaN})
    @DisplayName("0-100 мужаас гадуурх болон NaN оноонд exception шидэх ёстой")
    void outOfRangeScoresThrow(double score) {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(score));
    }

    @ParameterizedTest(name = "[{index}] {0} оноо → {1}")
    @CsvSource({
            "100,   A",
            "95,    A",
            "90,    A",
            "89.99, B",
            "80,    B",
            "79.99, C",
            "70,    C",
            "69.99, D",
            "60,    D",
            "59.99, F",
            "0,     F"
    })
    @DisplayName("Үсгэн дүнгийн бүх хязгаар (parameterized)")
    void letterGradeBoundaries(double score, String expected) {
        String actual = calc.letterGrade(score); // Act
        assertEquals(expected, actual);          // Assert
    }

    @Test
    @DisplayName("Бүх бүрэлдэхүүн дээд оноотой бол нийлбэр 100 (10+40+10+10+30)")
    void maxComponentsGiveHundred() {
        double att = 10, lab = 40, quiz1 = 10, quiz2 = 10, exam = 30;
        double total = calc.totalScore(att, lab, quiz1, quiz2, exam);
        assertEquals(100.0, total, DELTA);
    }

    @Test
    @DisplayName("Ирц сөрөг (att = -5) бол IllegalArgumentException шидэх ёстой")
    void negativeAttendanceThrows() {
        double att = -5;
        assertThrows(IllegalArgumentException.class,
                () -> calc.totalScore(att, 30, 8, 8, 20));
    }

    @Test
    @DisplayName("Лаб хязгаараас хэтэрсэн (lab = 41) бол IllegalArgumentException шидэх ёстой")
    void labAboveMaxThrows() {
        double lab = 41;
        assertThrows(IllegalArgumentException.class,
                () -> calc.totalScore(10, lab, 10, 10, 30));
    }

    @ParameterizedTest(name = "[{index}] {0}+{1}+{2}+{3}+{4} = {5}")
    @CsvSource({
            "10, 40,   10,  10, 30, 100",
            "0,  0,    0,   0,  0,  0",
            "9,  35,   8,   7,  25, 84",
            "10, 32.5, 7.5, 9,  21, 80",
            "5,  20,   5,   5,  15, 50"
    })
    @DisplayName("totalScore хүчинтэй оролтуудад зөв нийлбэр буцаана (parameterized)")
    void totalScoreValidInputs(double att, double lab, double q1, double q2, double exam,
                               double expected) {
        double total = calc.totalScore(att, lab, q1, q2, exam);
        assertEquals(expected, total, DELTA);
    }

    @ParameterizedTest(name = "[{index}] att={0}, lab={1}, q1={2}, q2={3}, exam={4} → exception")
    @CsvSource({
            "-0.1, 40,    10,   10,   30",
            "10.1, 40,    10,   10,   30",
            "10,   -1,    10,   10,   30",
            "10,   40.01, 10,   10,   30",
            "10,   40,    -1,   10,   30",
            "10,   40,    11,   10,   30",
            "10,   40,    10,   -1,   30",
            "10,   40,    10,   10.5, 30",
            "10,   40,    10,   10,   -1",
            "10,   40,    10,   10,   31"
    })
    @DisplayName("totalScore бүрэлдэхүүн бүрийн хязгаараас гарсан утгад exception (parameterized)")
    void totalScoreInvalidInputsThrow(double att, double lab, double q1, double q2, double exam) {
        assertThrows(IllegalArgumentException.class,
                () -> calc.totalScore(att, lab, q1, q2, exam));
    }
}
