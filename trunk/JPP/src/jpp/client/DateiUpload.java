package jpp.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class DateiUpload {
  // Construct data

  public static int upload(String serverURL, String filename,
      String contentType, Map<String, String> parameter, byte[] data,
      String proxy, int proxyPort) {
    try {

      // System.out.println(hpc.getProtocol());
      // System.out.println(hpc.getPort());

      // InputStream fis = con.openInputStream();
      // byte[] data = new byte[fis.available()];
      //
      // fis.readFully(data);
      // fis.close();
      if (proxy != null && proxy.length() > 0 && proxyPort > 0) {

        Properties props = System.getProperties();
        props.setProperty("http.proxyHost", proxy);
        props.setProperty("http.proxyPort", proxyPort + "");
      }
      URL url = new URL(serverURL);

      String boundary = MultiPartFormOutputStream.createBoundary();
      HttpURLConnection hpc = (HttpURLConnection) url.openConnection();
      hpc.setRequestMethod("POST");
      hpc.setRequestProperty("Accept", "*/*");
      hpc.setRequestProperty("Content-Type", MultiPartFormOutputStream
          .getContentType(boundary));
      // set some other request headers...
      hpc.setRequestProperty("Connection", "Keep-Alive");
      hpc.setRequestProperty("Cache-Control", "no-cache");
      // no need to connect cuz getOutputStream() does it
      hpc.setDoOutput(true);
      hpc.setDoInput(true);

      MultiPartFormOutputStream out = new MultiPartFormOutputStream(hpc
          .getOutputStream(), boundary);
      // write a text field element
      if (parameter != null) {
        for (Entry<String, String> entry : parameter.entrySet()) {

          out.writeField(entry.getKey(), entry.getValue());
        }
      }
      // upload a file
      if (data != null) {
        out.writeFile(filename, contentType, contentType,
            new ByteArrayInputStream(data));
      }
      // can also write bytes directly
      // out.writeFile("myFile", "text/plain", "C:\\test.txt",
      // "This is some file text.".getBytes("ASCII"));
      out.close();
      // read response from server
      InputStream in = hpc.getInputStream();
      String line = "";
      int c = -1;
      while ((c = in.read()) != -1) {
        line += (char) c;
      }
      System.out.println(line);
      in.close();

      System.out.println("Fertig");

      return hpc.getResponseCode();

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.toString());
    }

    return -1;
  }

  public static int upload(String hostname, String path, int port,
      String filename, String contentType, Map<String, String> parameter,
      byte[] data, String proxy, int proxyPort) {

    return upload(hostname + ":" + port + path, filename, contentType,
        parameter, data, proxy, proxyPort);
  }
}
