# Лаб 04 — JUnit 5 нэгжийн тест

**Оюутан:** Эрдэнэтөр Бүрэнбаяр
**Оюутны код:** B242270084

## Орчин

```
$ java -version
openjdk version "21.0.12.1" 2026-08-18
OpenJDK Runtime Environment (build 21.0.12.1+1-1-24.04.4-Ubuntu)
OpenJDK 64-Bit Server VM (build 21.0.12.1+1-1-24.04.4-Ubuntu, mixed mode, sharing)
```

```
$ mvn -version
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 21.0.12.1, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "7.0.0-34-generic", arch: "amd64", family: "unix"
```

## Тестийн тоо

- Тестийн методын тоо: **16** (`@Test` — 11, `@ParameterizedTest` — 5)
- `results/mvn-test.txt`-ийн сүүлийн `Tests run`: **43** (Failures: 0, Errors: 0, Skipped: 0, BUILD SUCCESS)
- Parameterized тест: letterGrade-д 2 (`letterGradeBoundaries` — 11 мөр, `outOfRangeScoresThrow` — 5 утга), totalScore-д 2 (`totalScoreValidInputs` — 5 мөр, `totalScoreInvalidInputsThrow` — 10 мөр)
- Хязгаарын утгууд: 90, 89.99, 60, 59.99, 0, 100 (тус бүр тусдаа `@Test`, мөн parameterized дотор)
- `assertThrows`: letterGrade (-1, 101, -0.01, 100.01, NaN …) ба totalScore (att = -5, lab = 41, бүрэлдэхүүн бүрийн сөрөг/хэтэрсэн утга)

## Мутацийн туршилт

`letterGrade` доторх `score >= 90`-ийг `score > 90` болгож `mvn test` ажиллуулахад (`results/mvn-test-mutant.txt`):

- `Tests run: 43, Failures: 2`, **BUILD FAILURE**
- `ninetyIsExactlyA` — `expected: <A> but was: <B>` (GradeCalculatorTest.java:48)
- `letterGradeBoundaries` [3] (`90, A`) — `expected: <A> but was: <B>` (GradeCalculatorTest.java:129)

Үүний дараа `>= 90`-ийг буцааж засаад `mvn test` дахин ажиллуулахад `results/mvn-test.txt` ногоон (43/43, BUILD SUCCESS).

## Миний дүгнэлт

<-- Энд өөрийнхөө үгээр 5-8 өгүүлбэр бич:
 - мутацид яагаад зөвхөн 2 тест унасан, бусад 41 нь яагаад давсан бэ
 - хязгаарын утга (90) дээр тест байгаагүй бол юу болох байсан бэ
 - аль тест хамгийн сонирхолтой санагдсан бэ (жишээ: NaN тест — `score < 0 || score > 100` нь NaN-ийг барьдаггүй)
 - юуг илрүүлж чадахгүй байна вэ (жишээ: 89.995 гэх мэт бутархай утга)
 Бичиж дуусаад энэ тайлбарын блокийг устга. -->
