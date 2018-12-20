package xyz.shpasha.androidtfs.source.remote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {

    private List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies =  cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (cookies != null)
            return cookies;
        return new ArrayList<>();
    }

    String getCsrfToken() {
        if (cookies == null) return null;

        for (Cookie c : cookies) {
            if (c.name().equals("csrftoken")) {
                return c.value();
            }
        }
        return null;
    }

    Set<String> getCookieStringSet() {
        Set<String> cookieSet = new HashSet<>();
        for (Cookie cookie : cookies) {
            cookieSet.add(cookie.toString());
        }
        return cookieSet;
    }

    void setCookies(Set<String> cookieSet) {
        cookies = new ArrayList<>();
        for (String s : cookieSet) {
            cookies.add(Cookie.parse(HttpUrl.parse(ApiService.API_URL), s));
        }
    }

}