package com.mcube.FreightRateCalculator.external;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GeocodingApiClientTest {

    @Test
    void isCityWordPresent1() {
        assertTrue(GeocodingApiClient.isCityWordPresent("город Ташкент"));
    }
    @Test
    void isCityWordPresent2() {
        assertTrue(GeocodingApiClient.isCityWordPresent("Узбекистан город Ташкент"));
    }

    @Test
    void isCityWordPresent3() {
        assertTrue(GeocodingApiClient.isCityWordPresent("Узбекистан г. Ташкент"));
    }

    @Test
    void isCityWordPresent4() {
        assertTrue(GeocodingApiClient.isCityWordPresent("Узбекистан г.Ташкент"));
    }

    @Test
    void isCityWordPresent5() {
        assertTrue(GeocodingApiClient.isCityWordPresent("г.Ташкент"));
    }

    @Test
    void isCityWordPresent6() {
        assertTrue(GeocodingApiClient.isCityWordPresent("г. Ташкент"));
    }

    @Test
    void isCityWordPresent7() {
        assertTrue(GeocodingApiClient.isCityWordPresent("ГороД Ташкент"));
    }

    @Test
    void isCityWordPresent8() {
        assertFalse(GeocodingApiClient.isCityWordPresent("городТашкент"));
    }

    @Test
    void isCityWordPresent9() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Новгород"));
    }

    @Test
    void isCityWordPresent10() {
        assertFalse(GeocodingApiClient.isCityWordPresent("г Ташкент"));
    }

    @Test
    void isCityWordPresent11() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Гамбург"));
    }

    @Test
    void isCityWordPresent12() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Миргород"));
    }
    @Test
    void isCityWordPresent13() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Городище"));
    }
    @Test
    void isCityWordPresent14() {
        assertTrue(GeocodingApiClient.isCityWordPresent("Г.ТАШКЕНТ"));
    }


    @Test
    void testIsCityWordPresent_withFullWord() {
        assertTrue(GeocodingApiClient.isCityWordPresent("город Москва"));
    }
    @Test
    void testIsCityWordPresent_withFullWord2() {
        assertTrue(GeocodingApiClient.isCityWordPresent("Это город Ташкент"));
    }
    @Test
    void testIsCityWordPresent_withFullWord3() {
        assertFalse(GeocodingApiClient.isCityWordPresent("город"));
    }

    @Test
    void testIsCityWordPresent_withFullWord4() {
        assertTrue(GeocodingApiClient.isCityWordPresent("много много текста город Ташкент и еще много текста"));
    }

    @Test
    void testIsCityWordPresent_withAbbreviation() {
        assertTrue(GeocodingApiClient.isCityWordPresent("г. Санкт-Петербург"));
    }

    @Test
    void testIsCityWordPresent_withAbbreviation2() {
        assertTrue(GeocodingApiClient.isCityWordPresent("г. Ташкент"));
    }

    @Test
    void testIsCityWordPresent_withoutCityWord() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Новгород")); // не "город Новгород"
    }

    @Test
    void testIsCityWordPresent_withoutCityWord2() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Киев"));
    }

    @Test
    void testIsCityWordPresent_withoutCityWord3() {
        assertFalse(GeocodingApiClient.isCityWordPresent("Hamburg"));
    }

    @Test
    void testIsCityWordPresent_withCaseInsensitive() {
        assertTrue(GeocodingApiClient.isCityWordPresent("Город Алматы"));
    }
    @Test
    void testIsCityWordPresent_withCaseInsensitive2() {
        assertTrue(GeocodingApiClient.isCityWordPresent("г. Бишкек"));
    }
    @Test
    void testIsCityWordPresent_withCaseInsensitive3() {
        assertTrue(GeocodingApiClient.isCityWordPresent("ГОРОД Самарканд"));
    }
}