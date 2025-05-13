package org.cordianatefinder.adapter;

import java.io.IOException;

public interface GeocodeHttpClient {
    String get(String url) throws IOException;
}