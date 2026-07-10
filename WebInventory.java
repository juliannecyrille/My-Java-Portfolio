import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WebInventory {

    // Enhanced Model Entity matching professional inventory details
    static class Appliance {
        int id;
        String name;
        String sku;
        String category;
        int stock;
        int minStock;
        int max;
        String supplier;
        String lastUpdated;

        Appliance(int id, String name, String sku, String category, int stock, int minStock, int max, String supplier, String lastUpdated) {
            this.id = id;
            this.name = name;
            this.sku = sku;
            this.category = category;
            this.stock = stock;
            this.minStock = minStock;
            this.max = max;
            this.supplier = supplier;
            this.lastUpdated = lastUpdated;
        }
    }

    private static List<Appliance> inventoryCluster = new ArrayList<>();
    private static int nextIdCounter = 0;

    public static void main(String[] args) throws IOException {
        // Baseline Seed Records with explicit SKUs, Minimum Restock Thresholds, and Suppliers
        inventoryCluster.add(new Appliance(nextIdCounter++, "Refrigerator", "SKU-REF-2026", "Kitchen Appliances", 8, 5, 15, "NexTech Supply", "Just now"));
        inventoryCluster.add(new Appliance(nextIdCounter++, "Microwave Oven", "SKU-MIC-9081", "Kitchen Appliances", 2, 3, 10, "Apex Wholesale", "Just now"));
        inventoryCluster.add(new Appliance(nextIdCounter++, "Blender", "SKU-BLN-4412", "Kitchen Appliances", 15, 4, 20, "NexTech Supply", "Just now"));
        inventoryCluster.add(new Appliance(nextIdCounter++, "Smart TV", "SKU-TV-5500", "Living Room Tech", 6, 2, 8, "Global Tech Dist", "Just now"));
        inventoryCluster.add(new Appliance(nextIdCounter++, "Air Conditioner", "SKU-AC-1200", "Living Room Tech", 1, 2, 5, "Apex Wholesale", "Just now"));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // HTML UI Portal Endpoint
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestURI().getPath().equals("/")) {
                    byte[] response = Files.readAllBytes(Paths.get("index.html"));
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, response.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response);
                    os.close();
                }
            }
        });

        // JSON Data Feed Endpoint
        server.createContext("/api/data", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                dispatchJSONPayload(exchange, compileClusterToJSON());
            }
        });

        // Interactive Stock Delta Handler
        server.createContext("/api/update", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                try {
                    String[] params = query.split("&");
                    int id = Integer.parseInt(params[0].split("=")[1]);
                    int qty = Integer.parseInt(params[1].split("=")[1]);
                    
                    for (Appliance app : inventoryCluster) {
                        if (app.id == id) {
                            if (qty >= 0 && qty <= app.max) {
                                app.stock = qty;
                                // Inject active system current system time stamp on changes
                                app.lastUpdated = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error mapping parameters.");
                }
                dispatchJSONPayload(exchange, compileClusterToJSON());
            }
        });

        // Dynamic Record Creation Mapping Endpoint
        server.createContext("/api/add", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                try {
                    String[] params = query.split("&");
                    String name = URLDecoder.decode(params[0].split("=")[1], StandardCharsets.UTF_8.name());
                    String category = URLDecoder.decode(params[1].split("=")[1], StandardCharsets.UTF_8.name());
                    int stock = Integer.parseInt(params[2].split("=")[1]);
                    int min = Integer.parseInt(params[3].split("=")[1]);
                    int max = Integer.parseInt(params[4].split("=")[1]);

                    // Generate automatic programmatic SKU descriptor tag strings
                    String cleanName = name.replaceAll("\\s+", "").substring(0, Math.min(name.length(), 3)).toUpperCase();
                    String generatedSku = "SKU-" + cleanName + "-" + (1000 + (int)(Math.random() * 9000));

                    inventoryCluster.add(new Appliance(nextIdCounter++, name, generatedSku, category, stock, min, max, "Standard Vendor", "Just now"));
                } catch (Exception e) {
                    System.out.println("Asset creation compilation failure.");
                }
                dispatchJSONPayload(exchange, compileClusterToJSON());
            }
        });

        System.out.println("🚀 Connected Production Server online!");
        System.out.println("👉 Workspace entry portal: http://localhost:8080");
        server.start();
    }

    private static void dispatchJSONPayload(HttpExchange exchange, String payload) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, payload.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(payload.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private static String compileClusterToJSON() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < inventoryCluster.size(); i++) {
            Appliance app = inventoryCluster.get(i);
            json.append(String.format(
                "{\"id\":%d,\"name\":\"%s\",\"sku\":\"%s\",\"category\":\"%s\",\"stock\":%d,\"minStock\":%d,\"max\":%d,\"supplier\":\"%s\",\"lastUpdated\":\"%s\"}", 
                app.id, app.name, app.sku, app.category, app.stock, app.minStock, app.max, app.supplier, app.lastUpdated
            ));
            if (i < inventoryCluster.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}