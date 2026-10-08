package eleftheria.code.serverResourcepack;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import eleftheria.code.serverResourcepack.events.Join;
import eleftheria.code.serverResourcepack.events.Load;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

public final class ServerResourcepack extends JavaPlugin {

    private static HttpServer server;
    public static String hash;

    @Override
    public void onEnable() {
        File rp = new File("resourcepack/pack.zip");
        if (!rp.exists()) {
            System.out.println("[ERROR] Resource pack not found: " + rp.getAbsolutePath());
        } else {
            System.out.println("resourcepack loaded");
            hash = calculateSha1(rp.toPath());

            try {
                server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8000), 0);
                server.createContext("/", (exchange) -> {
                    if ("GET".equals(exchange.getRequestMethod())) {
                        Headers head = exchange.getResponseHeaders();
                        head.add("Content-Type", "application/zip");
                        head.add("Content-Disposition", "attachment; filename=\"pack.zip\"");
                        head.add("Content-Length", String.valueOf(rp.length()));
                        head.add("Cache-Control", "no-cache");
                        exchange.sendResponseHeaders(200, rp.length());
                        OutputStream out = exchange.getResponseBody();
                        FileInputStream fileIn = new FileInputStream(rp);
                        byte[] buffer = new byte[8192];

                        int count;
                        while((count = fileIn.read(buffer)) != -1) {
                            out.write(buffer, 0, count);
                        }

                        out.flush();
                    }

                });
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Bukkit.getPluginManager().registerEvents(new Load(), this);
            Bukkit.getPluginManager().registerEvents(new Join(), this);
            System.out.println("resourcepack enabled");
        }
    }

    @Override
    public void onDisable() {
        server.stop(0);
    }

    private static String calculateSha1(Path file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            try (InputStream is = Files.newInputStream(file)) {
                byte[] buffer = new byte[8192];

                int read;
                while((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
            }

            byte[] hash = digest.digest();
            StringBuilder sb = new StringBuilder(40);

            for(byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
