package com.darshil.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class F1ApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String BASE = "https://f1api.dev/api";

    /**
     * Fetch drivers for a given year. Returns the "drivers" JsonNode array or null.
     */
    public JsonNode getDrivers(int year) {
        String url = String.format("%s/%d/drivers", BASE, year);
        return fetchArrayNode(url, "drivers");
    }

    /**
     * Try to fetch a list of races for a season. Many endpoints return one-per-round,
     * but /races endpoint may return an array — try that first, otherwise we'll return null.
     */
    public JsonNode getRacesForSeason(int year) {
        String url = String.format("%s/%d/races", BASE, year);
        return fetchArrayNode(url, null); // return the whole response if it's an array
    }

    /**
     * Fetch single race by season+round (e.g. /2025/1/race). Returns the "races" node (an object).
     */
    public JsonNode getRaceBySeasonRound(int year, int round) {
        String url = String.format("%s/%d/%d/race", BASE, year, round);
        try {
            String resp = restTemplate.getForObject(url, String.class);
            if (resp == null) return null;
            JsonNode root = mapper.readTree(resp);
            return root.path("races"); // this is an object in f1api examples
        } catch (HttpClientErrorException.NotFound nf) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ----------------- helpers ----------------- */

    private JsonNode fetchArrayNode(String url, String childName) {
        try {
            String resp = restTemplate.getForObject(url, String.class);
            if (resp == null) return null;
            JsonNode root = mapper.readTree(resp);

            if (childName != null) {
                JsonNode arr = root.path(childName);
                if (arr.isArray()) return arr;
                return null;
            } else {
                // return root directly (could be array or object)
                if (root.isArray()) return root;
                // sometimes API returns object with "races" key or similar
                if (root.has("races") && root.path("races").isArray()) return root.path("races");
                return null;
            }
        } catch (HttpClientErrorException.NotFound nf) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
