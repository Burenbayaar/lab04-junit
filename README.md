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

Мутацид (`score >= 90` → `score > 90`) зөвхөн 2 тест унасан, учир нь энэ өөрчлөлт програмын зан төлөвийг ганцхан цэг дээр —
оноо яг 90 байхад л — өөрчилдөг: `ninetyIsExactlyA` ба `letterGradeBoundaries`-ийн `(90, A)` мөр л яг 90-ийг шалгадаг тул
тэд "expected: <A> but was: <B>" гэж унасан. Бусад 41 тест 95, 89.99, 60, 59.99 гэх мэт утгуудыг ашигладаг бөгөөд эдгээр дээр
`>=` ба `>` хоёр ижил үр дүн өгдөг тул давсан — энэ нь тест олон байх нь чухал биш, *зөв цэгийг* шалгах нь чухал гэдгийг харуулсан.
Хэрэв 90 дээр тусгай тест байгаагүй бол мутант амьд үлдэж, яг 90 оноо авсан оюутан "A"-ийн оронд "B" авах алдаа бүх тест
ногоон байхад production руу орох байсан. Миний хувьд хамгийн сонирхолтой нь NaN тест байсан: `score < 0 || score > 100`
нөхцөл NaN-ийг барьдаггүй (NaN-тай харьцуулалт үргэлж false), тиймээс хязгаарын шалгалт "зөв" харагдсан ч NaN-д зориулсан
тусдаа шалгалт хэрэгтэйг энэ тест л илрүүлдэг. Гэхдээ миний тестүүд зөвхөн сонгосон цэгүүдийг шалгадаг: 89.995 гэх мэт
тестэлсэн хоёр утгын хоорондох бутархай утга, эсвэл бусад зааг (80, 70) дээрх ижил төрлийн мутацийг тус бүрд нь тест
бичээгүй бол илрүүлж чадахгүй. Иймд хязгаарын утгын шинжилгээг бүх зааг дээр системтэйгээр хийж, мутацийн туршилтаар
тестийнхээ хүчийг шалгах нь зөв гэж дүгнэлээ.

Мутаци хийхээс өмнө 43 тест байгаа болохоор олонхи нь унах байх гэж бодсон. Гэтэл ердөө 2 тест унахыг хараад тестийн тоо биш, аль утгыг шалгаж байгаа нь чухал гэдгийг ойлгосон.
NaN нь `score < 0 || score > 100` нөхцөлд баригддаггүйг өмнө нь мэддэггүй байсан. Энэ лабаас double-тай ажиллахдаа NaN-ийг тусад нь шалгах хэрэгтэйг сурсан.

## AI туслахын хэрэглээ

"Миний дүгнэлт" хэсгийн ноорогт Claude (Anthropic) AI туслахыг ашигласан; агуулгыг өөрийн `results/mvn-test-mutant.txt`
ба `results/mvn-test.txt` дахь бодит үр дүнтэй тулгаж, өөрийн ажиглалтаар засварласан.
