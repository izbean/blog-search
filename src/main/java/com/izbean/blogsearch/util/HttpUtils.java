package com.izbean.blogsearch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriBuilder;

import java.net.MalformedURLException;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    public static UriBuilder parseUri(UriBuilder uriBuilder, String front, String back) {
        String urlStr = front + (back != null ? back : "");

        try {
            URL url = new URL(urlStr);

            return uriBuilder
                    .scheme(url.getProtocol())
                    .host(url.getHost())
                    .port(url.getPort())
                    .path(url.getPath());

        } catch (MalformedURLException e) {
            throw new InternalError(String.format("Malformed url: front(%s), back(%s)", front, back));
        }
    }

    public static UriBuilder parseUri(UriBuilder uriBuilder, String front) {
        return parseUri(uriBuilder, front, null);
    }

}
