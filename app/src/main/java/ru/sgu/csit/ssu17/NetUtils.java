package ru.sgu.csit.ssu17;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

final class NetUtils {

    public static String httpGet(String httpUrl) throws IOException {
        final URL url = new URL(httpUrl);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String res;
        InputStream istream = null;
        ByteArrayOutputStream ostream = null;
        try {
            istream = conn.getInputStream();
            byte[] buf = new byte[32 * 1024];
            ostream = new ByteArrayOutputStream();
            while (true) {
                int bytesRead = istream.read(buf);
                if (bytesRead < 0)
                    break;
                ostream.write(buf, 0, bytesRead);
            }
            res = ostream.toString("UTF-8");
        } finally {
            if (istream != null)
                istream.close();
            if (ostream != null)
                ostream.close();
        }
        return res;
    }
}
